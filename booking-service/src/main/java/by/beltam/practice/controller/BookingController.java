package by.beltam.practice.controller;

import by.beltam.practice.dto.BookingCreateRequest;
import by.beltam.practice.dto.BookingResponse;
import by.beltam.practice.entity.Booking;
import by.beltam.practice.mapper.BookingMapper;
import by.beltam.practice.service.BookingService;
import by.beltam.practice.utility.BookingStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final BookingMapper mapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse createBooking(
            @AuthenticationPrincipal String userId,
            @Valid @RequestBody BookingCreateRequest request) {
        Booking savedBooking = bookingService.createBooking(UUID.fromString(userId), request);
        return mapper.toResponse(savedBooking);
    }

    @GetMapping("/my")
    public List<BookingResponse> getMyBookings(@AuthenticationPrincipal String userId) {
        return mapper.toResponseList(bookingService.getUserBookings(UUID.fromString(userId)));
    }

    @PatchMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelBooking(
            @AuthenticationPrincipal String userId,
            @PathVariable UUID id) {
        bookingService.updateStatus(id, BookingStatus.CANCELLED, UUID.fromString(userId), "USER");
    }

    @PatchMapping("/{id}/status")
    public BookingResponse updateStatus(
            @PathVariable UUID id,
            @RequestParam BookingStatus status,
            @AuthenticationPrincipal String userId,
            Authentication authentication) {

        String role = authentication.getAuthorities().iterator().next()
                .getAuthority().replace("ROLE_", "");

        Booking updated = bookingService.updateStatus(id, status, UUID.fromString(userId), role);
        return mapper.toResponse(updated);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingResponse> searchBookings(
            @RequestParam(required = false) UUID roomTypeId,
            @RequestParam(required = false) BookingStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        List<Booking> results = bookingService.searchBookings(roomTypeId, status, from, to);
        return mapper.toResponseList(results);
    }

    @GetMapping("/{id}")
    public BookingResponse getBooking(@PathVariable UUID id) {
        return mapper.toResponse(bookingService.getBookingById(id));
    }
}