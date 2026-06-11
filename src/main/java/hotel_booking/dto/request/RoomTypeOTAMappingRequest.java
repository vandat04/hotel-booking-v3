package hotel_booking.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomTypeOTAMappingRequest {
    private Integer roomTypeId;
    private String otaName;
    private String importIcalUrl;
}
