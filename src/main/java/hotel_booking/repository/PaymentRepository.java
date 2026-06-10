package hotel_booking.repository;

import hotel_booking.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByTransactionReference(String transactionReference);

    List<Payment> findByBooking_Id(Integer bookingId);

    List<Payment> findByBookingId(Integer bookingId);

    @Query("""
            SELECT p
            FROM Payment p
            WHERE
                (
                    :keyword IS NULL
                    OR LOWER(p.transactionReference)
                        LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                    OR LOWER(p.gatewayName)
                        LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                    OR LOWER(CAST(p.paymentType AS string))
                        LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                )
            AND
                (
                    :status IS NULL
                    OR LOWER(CAST(p.status AS string)) = LOWER(CAST(:status AS string))
                )
            AND
                (
                    :paymentMethod IS NULL
                    OR LOWER(CAST(p.paymentMethod AS string))
                        = LOWER(CAST(:paymentMethod AS string))
                )
            ORDER BY p.paymentDate DESC
            """)
    Page<Payment> searchPayments(
            @org.springframework.data.repository.query.Param("keyword") String keyword,
            @org.springframework.data.repository.query.Param("status") String status,
            @org.springframework.data.repository.query.Param("paymentMethod") String paymentMethod,
            Pageable pageable
    );

    @Query(value = """
            SELECT p
            FROM Payment p
            JOIN p.booking b
            WHERE
                (
                    :keyword IS NULL
                    OR LOWER(p.transactionReference)
                        LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                    OR LOWER(p.gatewayName)
                        LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                    OR LOWER(CAST(p.paymentType AS string))
                        LIKE LOWER(CONCAT('%', CAST(:keyword AS string), '%'))
                )
            AND
                (
                    :status IS NULL
                    OR LOWER(CAST(p.status AS string)) = LOWER(CAST(:status AS string))
                )
            AND
                (
                    :paymentMethod IS NULL
                    OR LOWER(CAST(p.paymentMethod AS string))
                        = LOWER(CAST(:paymentMethod AS string))
                )
            AND
                (
                    :bookingSource IS NULL
                    OR LOWER(CAST(b.bookingSource AS string))
                        = LOWER(CAST(:bookingSource AS string))
                )
            AND
                (
                    :otaChannel IS NULL
                    OR LOWER(CAST(b.bookingSource AS string))
                        = LOWER(CAST(:otaChannel AS string))
                )
            AND
                (
                    CAST(:fromDate AS timestamp) IS NULL
                    OR p.paymentDate >= :fromDate
                )
            AND
                (
                    CAST(:toDate AS timestamp) IS NULL
                    OR p.paymentDate <= :toDate
                )
            AND
                (
                    CAST(:minAmount AS double) IS NULL
                    OR p.amount >= :minAmount
                )
            AND
                (
                    CAST(:maxAmount AS double) IS NULL
                    OR p.amount <= :maxAmount
                )
            ORDER BY p.paymentDate DESC
            """)
    Page<Payment> filterPayments(
            @org.springframework.data.repository.query.Param("keyword") String keyword,
            @org.springframework.data.repository.query.Param("status") String status,
            @org.springframework.data.repository.query.Param("paymentMethod") String paymentMethod,
            @org.springframework.data.repository.query.Param("bookingSource") String bookingSource,
            @org.springframework.data.repository.query.Param("otaChannel") String otaChannel,
            @org.springframework.data.repository.query.Param("fromDate") LocalDateTime fromDate,
            @org.springframework.data.repository.query.Param("toDate") LocalDateTime toDate,
            @org.springframework.data.repository.query.Param("minAmount") BigDecimal minAmount,
            @org.springframework.data.repository.query.Param("maxAmount") BigDecimal maxAmount,
            Pageable pageable
    );

    @EntityGraph(attributePaths = {
            "booking",
            "booking.roomType",
            "booking.roomSchedules",
            "booking.roomSchedules.room",
            "invoice"
    })
    Optional<Payment> findDetailById(Integer id);

    // TOTAL REVENUE=========================================================
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.status = 'SUCCESS'
            """)
    BigDecimal getTotalRevenue();

    // TODAY REVENUE =========================================================
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.status = 'SUCCESS'
            AND p.paymentDate BETWEEN :start AND :end
            """)
    BigDecimal getRevenueBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    // TOTAL TRANSACTIONS=========================================================
    @Query("""
            SELECT COUNT(p)
            FROM Payment p
            """)
    Long getTotalTransactions();

    // TOTAL REFUND =========================================================
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.status = 'REFUNDED'
            """)
    BigDecimal getTotalRefund();

    // OTA REVENUE=========================================================
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.status = 'SUCCESS'
            AND p.booking.bookingSource IN (
                'AGODA',
                'BOOKING',
                'EXPEDIA'
            )
            """)
    BigDecimal getOtaRevenue();

    // OCCUPANCY REVENUE=========================================================
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.status = 'SUCCESS'
            AND p.booking.status = 'CHECKED_OUT'
            """)
    BigDecimal getOccupancyRevenue();

    // =========================================================
    // ROOM REVENUE
    // =========================================================
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.status = 'SUCCESS'
            AND p.paymentType IN (
                'FULL_ROOM_CHARGE',
                'EXTEND_ROOM_CHARGE'
            )
            """)
    BigDecimal getRoomRevenue();

    // =========================================================
    // WALK-IN REVENUE
    // =========================================================
    @Query("""
            SELECT COALESCE(SUM(p.amount), 0)
            FROM Payment p
            WHERE p.status = 'SUCCESS'
            AND p.booking.bookingSource = 'WALK-IN'
            """)
    BigDecimal getWalkInRevenue();
}
