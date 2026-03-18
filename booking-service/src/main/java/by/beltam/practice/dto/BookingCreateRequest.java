package by.beltam.practice.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record BookingCreateRequest(

        @NotNull
        UUID roomTypeId,

        @NotNull
        @FutureOrPresent
        LocalDate checkIn,

        @NotNull
        @Future
        LocalDate checkOut
) {
}