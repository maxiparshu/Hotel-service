package by.beltam.practice.repository;

import by.beltam.practice.entity.Booking;
import by.beltam.practice.utility.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
    @Query(value = """
                SELECT count(b) from Booking b
                WHERE b.roomTypeId = :roomTypeId
                AND b.status IN (
                    by.beltam.practice.utility.BookingStatus.PENDING,
                    by.beltam.practice.utility.BookingStatus.CONFIRMED,
                    by.beltam.practice.utility.BookingStatus.CHECKED_IN
                )
                AND b.checkIn < :checkOut
                AND b.checkOut > :checkIn
            """)
    long countActiveOverlappingBookings(
            @Param("roomTypeId") UUID roomTypeId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut
    );
    List<Booking> findAllByStatusAndCheckOutBefore(BookingStatus status, LocalDate date);
    List<Booking> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

}
