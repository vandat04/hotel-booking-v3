package hotel_booking.controller;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import hotel_booking.entity.RoomSchedule;
import hotel_booking.entity.RoomType;
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

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ical")
@RequiredArgsConstructor
public class ICalController {

    private final RoomTypeRepository roomTypeRepository;
    private final RoomScheduleRepository roomScheduleRepository;

    @GetMapping(value = "/room-type/{id}.ics", produces = "text/calendar")
    public ResponseEntity<String> exportRoomTypeICal(@PathVariable("id") Integer id) {
        Optional<RoomType> roomTypeOpt = roomTypeRepository.findById(id);
        if (roomTypeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Room Type not found");
        }

        // Fetch all active/scheduled room blocks
        List<RoomSchedule> schedules = roomScheduleRepository.findActiveSchedulesByRoomTypeId(id);

        ICalendar ical = new ICalendar();
        ical.setProductId("-//CheckinX//Hotel Management System//EN");

        for (RoomSchedule schedule : schedules) {
            VEvent event = new VEvent();
            
            // Convert LocalDateTime to java.util.Date
            Date startDate = Timestamp.valueOf(schedule.getStartAt());
            Date endDate = Timestamp.valueOf(schedule.getEndAt());

            // Set dates with hasTime=false so that it outputs VALUE=DATE (no time, just calendar days)
            event.setDateStart(startDate, false);
            event.setDateEnd(endDate, false);
            
            event.setSummary("Reserved - CheckinX Room " + schedule.getRoom().getRoomNumber());
            event.setUid("schedule-" + schedule.getId() + "@checkinx.com");

            ical.addEvent(event);
        }

        String icalString = Biweekly.write(ical).go();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", "room-type-" + id + ".ics");
        headers.setContentType(MediaType.valueOf("text/calendar;charset=UTF-8"));

        return new ResponseEntity<>(icalString, headers, HttpStatus.OK);
    }
}
