package hotel_booking.repository;

import hotel_booking.entity.RoomTypeOTAMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomTypeOTAMappingRepository extends JpaRepository<RoomTypeOTAMapping, Integer> {
    
    List<RoomTypeOTAMapping> findByRoomTypeId(Integer roomTypeId);
    
    Optional<RoomTypeOTAMapping> findByRoomTypeIdAndOtaNameIgnoreCase(Integer roomTypeId, String otaName);
}
