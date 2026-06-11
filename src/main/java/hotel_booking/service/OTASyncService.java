package hotel_booking.service;

import biweekly.Biweekly;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import hotel_booking.dto.request.CheckAvailabilityRequest;
import hotel_booking.dto.request.RoomTypeOTAMappingRequest;
import hotel_booking.dto.response.CheckAvailabilityResponse;
import hotel_booking.entity.*;
import hotel_booking.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OTASyncService {

    private static final Logger log = LoggerFactory.getLogger(OTASyncService.class);

    private final RoomTypeOTAMappingRepository roomTypeOTAMappingRepository;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private final RoomScheduleRepository roomScheduleRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final BookingService bookingService;
    private final PaymentRepository paymentRepository;

    // Cron job to run every 1 minute (60,000 ms) for near real-time sync
    @Scheduled(fixedRate = 60000)
    public void syncAllOTACalendars() {
        log.info("Starting OTA Calendar Sync...");
        List<RoomTypeOTAMapping> mappings = roomTypeOTAMappingRepository.findAll();
        for (RoomTypeOTAMapping mapping : mappings) {
            if (mapping.getImportIcalUrl() == null || mapping.getImportIcalUrl().isBlank()) {
                continue;
            }
            try {
                syncSingleCalendar(mapping);
            } catch (Exception e) {
                log.error("Failed to sync OTA calendar for RoomType={}, OTA={}: {}", 
                        mapping.getRoomType().getName(), mapping.getOtaName(), e.getMessage());
            }
        }
        log.info("OTA Calendar Sync completed.");
    }

    public List<RoomTypeOTAMapping> getAllMappings() {
        return roomTypeOTAMappingRepository.findAll();
    }

    public RoomTypeOTAMapping createMapping(RoomTypeOTAMappingRequest request) {
        RoomType roomType = roomTypeRepository.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("ROOM_TYPE_NOT_FOUND"));

        // Check if mapping already exists
        Optional<RoomTypeOTAMapping> existing = roomTypeOTAMappingRepository
                .findByRoomTypeIdAndOtaNameIgnoreCase(request.getRoomTypeId(), request.getOtaName());
        
        if (existing.isPresent()) {
            RoomTypeOTAMapping mapping = existing.get();
            mapping.setImportIcalUrl(request.getImportIcalUrl());
            return roomTypeOTAMappingRepository.save(mapping);
        }

        RoomTypeOTAMapping mapping = RoomTypeOTAMapping.builder()
                .roomType(roomType)
                .otaName(request.getOtaName().toUpperCase())
                .importIcalUrl(request.getImportIcalUrl())
                .build();

        return roomTypeOTAMappingRepository.save(mapping);
    }

    public void deleteMapping(Integer id) {
        if (!roomTypeOTAMappingRepository.existsById(id)) {
            throw new RuntimeException("MAPPING_NOT_FOUND");
        }
        roomTypeOTAMappingRepository.deleteById(id);
    }

    public void syncMapping(Integer id) throws Exception {
        RoomTypeOTAMapping mapping = roomTypeOTAMappingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MAPPING_NOT_FOUND"));
        syncSingleCalendar(mapping);
    }

    @Transactional
    public void syncSingleCalendar(RoomTypeOTAMapping mapping) throws Exception {
        log.info("Syncing calendar from URL: {}", mapping.getImportIcalUrl());
        
        URL url = new URL(mapping.getImportIcalUrl());
        ICalendar ical;
        try (InputStream in = url.openStream()) {
            ical = Biweekly.parse(in).first();
        }

        if (ical == null) {
            log.warn("Empty or invalid iCal content from URL: {}", mapping.getImportIcalUrl());
            return;
        }

        Set<String> activeOtaUids = new HashSet<>();
        List<VEvent> events = ical.getEvents();

        for (VEvent event : events) {
            if (event.getUid() == null || event.getDateStart() == null || event.getDateEnd() == null) {
                continue;
            }

            String uid = event.getUid().getValue();
            activeOtaUids.add(uid);

            Date rawStart = event.getDateStart().getValue();
            Date rawEnd = event.getDateEnd().getValue();

            // Normalize check-in / check-out times (14:00 checkin, 12:00 checkout)
            LocalDateTime checkIn = rawStart.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(14, 0);
            LocalDateTime checkOut = rawEnd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(12, 0);

            Optional<Booking> bookingOpt = bookingRepository.findByBookingSourceAndUid(mapping.getOtaName(), uid);

            if (bookingOpt.isEmpty()) {
                // Check availability
                CheckAvailabilityRequest availRequest = CheckAvailabilityRequest.builder()
                        .roomTypeId(mapping.getRoomType().getId())
                        .checkIn(checkIn)
                        .checkOut(checkOut)
                        .numberOfRoom(1)
                        .bookingType("DAILY")
                        .build();

                CheckAvailabilityResponse availResponse = bookingService.checkAvailability(availRequest);

                if (availResponse.getAvailable() && !availResponse.getListRoomCanBook().isEmpty()) {
                    // Create booking
                    Booking booking = Booking.builder()
                            .customer(null)
                            .customerName("OTA Guest (" + mapping.getOtaName() + ")")
                            .customerPhone("")
                            .customerEmail("")
                            .roomType(mapping.getRoomType())
                            .requestedQuantity(1)
                            .requestedCheckin(checkIn)
                            .requestedCheckout(checkOut)
                            .bookingType("DAILY")
                            .bookingSource(mapping.getOtaName())
                            .status("CONFIRMED")
                            .paymentStatus("PAID")
                            .totalAmount(BigDecimal.ZERO)
                            .notes("Imported from iCal. UID: " + uid)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    bookingRepository.save(booking);

                    // Create Payment (Transaction)
                    Payment payment = Payment.builder()
                            .booking(booking)
                            .amount(BigDecimal.ZERO)
                            .paymentMethod("OTA")
                            .gatewayName(mapping.getOtaName())
                            .paymentType("BOOKING")
                            .status("SUCCESS")
                            .transactionReference(uid)
                            .paymentDate(LocalDateTime.now())
                            .notes("Initial payment created from iCal sync. UID: " + uid)
                            .build();
                    paymentRepository.save(payment);

                    // Allocate Room
                    Integer roomId = availResponse.getListRoomCanBook().get(0);
                    Room room = roomRepository.findById(roomId).orElseThrow();

                    RoomSchedule schedule = RoomSchedule.builder()
                            .booking(booking)
                            .room(room)
                            .startAt(checkIn)
                            .endAt(checkOut)
                            .status("SCHEDULED")
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    roomScheduleRepository.save(schedule);
                    log.info("Created Booking & RoomSchedule from OTA UID: {}, Room: {}", uid, room.getRoomNumber());
                } else {
                    // Overbooked: create booking but no RoomSchedule block
                    Booking booking = Booking.builder()
                            .customer(null)
                            .customerName("OTA Guest (" + mapping.getOtaName() + ") - OVERBOOKED")
                            .customerPhone("")
                            .customerEmail("")
                            .roomType(mapping.getRoomType())
                            .requestedQuantity(1)
                            .requestedCheckin(checkIn)
                            .requestedCheckout(checkOut)
                            .bookingType("DAILY")
                            .bookingSource(mapping.getOtaName())
                            .status("CONFIRMED")
                            .paymentStatus("PAID")
                            .totalAmount(BigDecimal.ZERO)
                            .notes("Imported from iCal. OVERBOOKED (No room available). UID: " + uid)
                            .createdAt(LocalDateTime.now())
                            .updatedAt(LocalDateTime.now())
                            .build();

                    bookingRepository.save(booking);

                    // Create Payment (Transaction) for overbooked case
                    Payment payment = Payment.builder()
                            .booking(booking)
                            .amount(BigDecimal.ZERO)
                            .paymentMethod("OTA")
                            .gatewayName(mapping.getOtaName())
                            .paymentType("BOOKING")
                            .status("SUCCESS")
                            .transactionReference(uid)
                            .paymentDate(LocalDateTime.now())
                            .notes("Initial payment created from iCal sync (OVERBOOKED). UID: " + uid)
                            .build();
                    paymentRepository.save(payment);
                    log.warn("Overbooked for OTA UID: {}. Booking created without schedule.", uid);
                }
            } else {
                // Booking already exists: check if dates have changed
                Booking booking = bookingOpt.get();
                if (!booking.getRequestedCheckin().isEqual(checkIn) || !booking.getRequestedCheckout().isEqual(checkOut)) {
                    log.info("Booking dates changed for OTA UID: {}. Re-allocating...", uid);
                    // Cancel old schedules
                    List<RoomSchedule> oldSchedules = roomScheduleRepository.findByBookingId(booking.getId());
                    for (RoomSchedule rs : oldSchedules) {
                        rs.setStatus("CANCELLED");
                        rs.setUpdatedAt(LocalDateTime.now());
                        roomScheduleRepository.save(rs);
                    }

                    // Check availability for new dates
                    CheckAvailabilityRequest availRequest = CheckAvailabilityRequest.builder()
                            .roomTypeId(mapping.getRoomType().getId())
                            .checkIn(checkIn)
                            .checkOut(checkOut)
                            .numberOfRoom(1)
                            .bookingType("DAILY")
                            .build();

                    CheckAvailabilityResponse availResponse = bookingService.checkAvailability(availRequest);

                    booking.setRequestedCheckin(checkIn);
                    booking.setRequestedCheckout(checkOut);
                    booking.setUpdatedAt(LocalDateTime.now());

                    if (availResponse.getAvailable() && !availResponse.getListRoomCanBook().isEmpty()) {
                        Integer roomId = availResponse.getListRoomCanBook().get(0);
                        Room room = roomRepository.findById(roomId).orElseThrow();

                        RoomSchedule schedule = RoomSchedule.builder()
                                .booking(booking)
                                .room(room)
                                .startAt(checkIn)
                                .endAt(checkOut)
                                .status("SCHEDULED")
                                .createdAt(LocalDateTime.now())
                                .updatedAt(LocalDateTime.now())
                                .build();

                        roomScheduleRepository.save(schedule);
                        booking.setNotes("Imported from iCal. Rescheduled. UID: " + uid);
                        log.info("Rescheduled OTA UID: {} successfully to Room: {}", uid, room.getRoomNumber());
                    } else {
                        booking.setNotes("Imported from iCal. Rescheduled - OVERBOOKED. UID: " + uid);
                        log.warn("Rescheduled OTA UID: {} failed due to overbooking.", uid);
                    }
                    bookingRepository.save(booking);
                }
            }
        }

        // Process cancellations (bookings present in DB but missing from the fresh .ics file)
        List<Booking> dbActiveBookings = bookingRepository.findAll();
        for (Booking booking : dbActiveBookings) {
            if (booking.getBookingSource() != null && booking.getBookingSource().equalsIgnoreCase(mapping.getOtaName()) 
                && "CONFIRMED".equalsIgnoreCase(booking.getStatus())
                && booking.getRoomType().getId().equals(mapping.getRoomType().getId())) {
                
                String notes = booking.getNotes();
                if (notes != null && notes.contains("UID: ")) {
                    String uid = notes.substring(notes.indexOf("UID: ") + 5).trim();
                    if (!activeOtaUids.contains(uid)) {
                        log.info("OTA Booking UID: {} is no longer in the iCal feed. Cancelling booking...", uid);
                        booking.setStatus("CANCELLED");
                        booking.setUpdatedAt(LocalDateTime.now());
                        bookingRepository.save(booking);

                        List<RoomSchedule> schedules = roomScheduleRepository.findByBookingId(booking.getId());
                        for (RoomSchedule rs : schedules) {
                            rs.setStatus("CANCELLED");
                            rs.setUpdatedAt(LocalDateTime.now());
                            roomScheduleRepository.save(rs);
                        }
                    }
                }
            }
        }

        mapping.setLastSyncedAt(LocalDateTime.now());
        roomTypeOTAMappingRepository.save(mapping);
    }
}
