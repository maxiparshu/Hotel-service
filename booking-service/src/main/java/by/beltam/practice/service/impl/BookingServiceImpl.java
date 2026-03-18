package by.beltam.practice.service.impl;

import by.beltam.practice.client.CatalogClient;
import by.beltam.practice.common.dto.catalog.RoomTypeDTO;
import by.beltam.practice.common.dto.payment.PaymentRequestEvent;
import by.beltam.practice.common.exception.BadRequestException;
import by.beltam.practice.common.exception.ConflictException;
import by.beltam.practice.common.exception.NotFoundException;
import by.beltam.practice.config.RabbitConfig;
import by.beltam.practice.dto.BookingCreateRequest;
import by.beltam.practice.entity.Booking;
import by.beltam.practice.exception.AccessDeniedException;
import by.beltam.practice.repository.BookingRepository;
import by.beltam.practice.service.BookingService;
import by.beltam.practice.utility.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CatalogClient catalogClient;
    private final RabbitTemplate rabbitTemplate;

    @Override
    @Transactional
    public Booking createBooking(UUID userId, BookingCreateRequest request) {
        if (!request.checkOut().isAfter(request.checkIn())) {
            throw new BadRequestException("Wrong dates sequence");
        }

        RoomTypeDTO roomType = catalogClient.getRoomType(request.roomTypeId());
        long occupied = bookingRepository.countActiveOverlappingBookings(
                request.roomTypeId(), request.checkIn(), request.checkOut());

        if (occupied >= roomType.totalRooms()) {
            throw new ConflictException("Hotel does not have enough rooms");
        }

        long nights = ChronoUnit.DAYS.between(request.checkIn(), request.checkOut());
        BigDecimal totalPrice = roomType.basePrice().multiply(BigDecimal.valueOf(nights));

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setRoomTypeId(request.roomTypeId());
        booking.setCheckIn(request.checkIn());
        booking.setCheckOut(request.checkOut());
        booking.setTotalPrice(totalPrice);
        booking.setStatus(BookingStatus.PENDING);

        Booking saved = bookingRepository.save(booking);

        log.info("Sending payment request for booking [{}]", saved.getId());

        PaymentRequestEvent event = new PaymentRequestEvent(
                saved.getId(),
                userId,
                saved.getTotalPrice(),
                "USD"
        );
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                log.info("Transaction committed. Sending payment request for booking [{}]", saved.getId());
                rabbitTemplate.convertAndSend(
                        RabbitConfig.BOOKING_EXCHANGE,
                        RabbitConfig.BOOKING_ROUTING_KEY,
                        event
                );
            }
        });

        return saved;
    }

    @Override
    @Transactional
    public Booking updateStatus(UUID bookingId, BookingStatus newStatus, UUID currentUserId, String role) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking"));

        boolean isAdmin = "ADMIN".equals(role);
        boolean isSystem = "SYSTEM".equals(role);

        if (!isSystem && !isAdmin && !booking.getUserId().equals(currentUserId)) {
            throw new AccessDeniedException("cannot access not your booking");
        }

        if (!isSystem && !isAdmin && newStatus != BookingStatus.CANCELLED) {
            throw new AccessDeniedException("can only cancel bookings");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED || booking.getStatus() == BookingStatus.CANCELLED) {
            throw new ConflictException("final statuses cannot be changed");
        }

        booking.setStatus(newStatus);
        return bookingRepository.save(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> searchBookings(UUID roomTypeId, BookingStatus status, LocalDate from, LocalDate to) {
        return bookingRepository.findAll().stream()
                .filter(b -> roomTypeId == null || b.getRoomTypeId().equals(roomTypeId))
                .filter(b -> status == null || b.getStatus() == status)
                .filter(b -> from == null || !b.getCheckIn().isBefore(from))
                .filter(b -> to == null || !b.getCheckOut().isAfter(to))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getUserBookings(UUID userId) {
        return bookingRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking"));
    }

    @Override
    @Transactional
    @Scheduled(cron = "0 0 1 * * *")
    public void processExpiredBookings() {
        List<Booking> expired = bookingRepository.findAllByStatusAndCheckOutBefore(
                BookingStatus.PENDING, LocalDate.now());

        expired.forEach(b -> b.setStatus(BookingStatus.CANCELLED));
        bookingRepository.saveAll(expired);
    }
}