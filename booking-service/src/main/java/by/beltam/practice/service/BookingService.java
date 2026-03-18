package by.beltam.practice.service;

import by.beltam.practice.dto.BookingCreateRequest;
import by.beltam.practice.entity.Booking;
import by.beltam.practice.utility.BookingStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public interface BookingService {
    Booking createBooking(UUID userId, BookingCreateRequest request);

    Booking updateStatus(UUID bookingId, BookingStatus newStatus, UUID currentUserId, String role);

    List<Booking> searchBookings(UUID roomTypeId, BookingStatus status, LocalDate from, LocalDate to);

    List<Booking> getUserBookings(UUID userId);

    Booking getBookingById(UUID bookingId);

    void processExpiredBookings();
}
