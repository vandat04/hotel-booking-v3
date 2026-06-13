package hotel_booking.controller;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import hotel_booking.entity.Room;
import hotel_booking.entity.RoomSchedule;
import hotel_booking.entity.RoomType;
import hotel_booking.repository.RoomRepository;
import hotel_booking.repository.RoomScheduleRepository;
import hotel_booking.repository.RoomTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ical")
@RequiredArgsConstructor
public class ICalController {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomScheduleRepository roomScheduleRepository;
    private final RoomRepository roomRepository;

    @GetMapping(value = "/room-type/{id}.ics", produces = "text/calendar")
    public ResponseEntity<String> exportRoomTypeICal(@PathVariable("id") Integer id) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findById(id);
        if (roomTypeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room Type not found");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "room-type-" + id + ".ics");
        headers.setContentType(MediaType.valueOf("text/calendar;charset=UTF-8"));

        LocalDate today = LocalDate.now();
        int totalRooms = roomRepository.countTotalRooms(id);
        if (totalRooms <= 0) {
            // If no rooms exist/are active, block everything for the next 365 days
            ICalendar ical = new ICalendar();
            ical.setProductId("-//CheckinX//Hotel Management System//EN");
            VEvent event = new VEvent();
            event.setDateStart(java.sql.Date.valueOf(today), false);
            event.setDateEnd(java.sql.Date.valueOf(today.plusDays(365)), false);
            event.setSummary("Closed - No Rooms Available");
            event.setUid("no-rooms-" + id + "@checkinx.com");
            ical.addEvent(event);
            return new ResponseEntity<>(Biweekly.write(ical).go(), headers, HttpStatus.OK);
        }

        // Fetch all active/scheduled room blocks
        List<RoomSchedule> schedules = roomScheduleRepository.findActiveSchedulesByRoomTypeId(id);

        List<LocalDate> blockedDates = new ArrayList<>();
        for (int i = 0; i < 365; i++) {
            LocalDate day = today.plusDays(i);

            // Count overlapping schedules for this date (from check-in to check-out)
            long overlappingCount = 0;
            for (RoomSchedule schedule : schedules) {
                LocalDate start = schedule.getStartAt().toLocalDate();
                LocalDate end = schedule.getEndAt().toLocalDate();
                if (!day.isBefore(start) && day.isBefore(end)) {
                    overlappingCount++;
                }
            }

            // Block only when 100% of rooms are occupied (available rooms = 0)
            if (overlappingCount >= totalRooms) {
                blockedDates.add(day);
            }
        }

        ICalendar ical = new ICalendar();
        ical.setProductId("-//CheckinX//Hotel Management System//EN");

        if (!blockedDates.isEmpty()) {
            LocalDate blockStart = blockedDates.get(0);
            LocalDate prevDate = blockStart;

            for (int i = 1; i < blockedDates.size(); i++) {
                LocalDate currentDate = blockedDates.get(i);
                if (currentDate.equals(prevDate.plusDays(1))) {
                    prevDate = currentDate;
                } else {
                    // Gap detected, write previous block
                    VEvent event = new VEvent();
                    event.setDateStart(java.sql.Date.valueOf(blockStart), false);
                    event.setDateEnd(java.sql.Date.valueOf(prevDate.plusDays(1)), false);
                    event.setSummary("Reserved - CheckinX Fully Booked");
                    event.setUid("block-" + id + "-" + blockStart + "@checkinx.com");
                    ical.addEvent(event);

                    blockStart = currentDate;
                    prevDate = currentDate;
                }
            }
            // Write the last block
            VEvent event = new VEvent();
            event.setDateStart(java.sql.Date.valueOf(blockStart), false);
            event.setDateEnd(java.sql.Date.valueOf(prevDate.plusDays(1)), false);
            event.setSummary("Reserved - CheckinX Fully Booked");
            event.setUid("block-" + id + "-" + blockStart + "@checkinx.com");
            ical.addEvent(event);
        }

        String icalString = Biweekly.write(ical).go();
        return new ResponseEntity<>(icalString, headers, HttpStatus.OK);
    }
}
