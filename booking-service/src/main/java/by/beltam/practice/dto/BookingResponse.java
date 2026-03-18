package by.beltam.practice.dto;

import by.beltam.practice.utility.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record BookingResponse(
        UUID id,
        UUID userId,
        UUID roomTypeId,
        LocalDate checkIn,
        LocalDate checkOut,
        BigDecimal totalPrice,
        BookingStatus status,
        Instant createdAt
) {
}