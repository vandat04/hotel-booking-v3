package hotel_booking.repository;

import hotel_booking.entity.OTAChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OTAChannelRepository extends JpaRepository<OTAChannel, Integer> {

    Optional<OTAChannel> findByName(String name);

    boolean existsByName(String name);

    @Query("""
            SELECT o
            FROM OTAChannel o
            WHERE
                (:searchPattern IS NULL
                    OR LOWER(o.name) LIKE :searchPattern
                    OR LOWER(o.otaHotelId) LIKE :searchPattern)
            AND (:isActive IS NULL
                    OR o.isActive = :isActive)
            """)
    Page<OTAChannel> filterOTAChannels(
            @Param("searchPattern") String searchPattern,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    boolean existsByNameAndIdNot(String name, Integer id);

    long countByIsActiveTrue();

    long countByIsActiveFalse();
}
