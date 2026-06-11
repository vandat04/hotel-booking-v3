package hotel_booking.controller;

import hotel_booking.dto.request.RoomTypeOTAMappingRequest;
import hotel_booking.dto.response.ApiResponse;
import hotel_booking.entity.RoomTypeOTAMapping;
import hotel_booking.service.OTASyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/ota-mappings")
@RequiredArgsConstructor
public class AdminOTAMappingController {

    private final OTASyncService otaSyncService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RoomTypeOTAMapping>>> getAllMappings() {
        List<RoomTypeOTAMapping> mappings = otaSyncService.getAllMappings();
        return ResponseEntity.ok(ApiResponse.success("Get OTA mappings successfully", mappings));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoomTypeOTAMapping>> createMapping(
            @RequestBody RoomTypeOTAMappingRequest request
    ) {
        RoomTypeOTAMapping mapping = otaSyncService.createMapping(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Create OTA mapping successfully", mapping));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMapping(@PathVariable Integer id) {
        otaSyncService.deleteMapping(id);
        return ResponseEntity.ok(ApiResponse.success("Delete OTA mapping successfully", null));
    }

    @PostMapping("/{id}/sync")
    public ResponseEntity<ApiResponse<String>> syncMapping(@PathVariable Integer id) {
        try {
            otaSyncService.syncMapping(id);
            return ResponseEntity.ok(ApiResponse.success("Sync calendar successfully", "SYNC_SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to sync calendar: " + e.getMessage()));
        }
    }

    @PostMapping("/sync-all")
    public ResponseEntity<ApiResponse<String>> syncAllMappings() {
        try {
            otaSyncService.syncAllOTACalendars();
            return ResponseEntity.ok(ApiResponse.success("Sync all calendars successfully", "SYNC_ALL_SUCCESS"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to sync all calendars: " + e.getMessage()));
        }
    }
}
