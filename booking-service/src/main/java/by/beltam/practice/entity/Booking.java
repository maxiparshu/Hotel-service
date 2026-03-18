package by.beltam.practice.entity;

import by.beltam.practice.utility.BookingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    private UUID roomTypeId;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private Instant createdAt;

    @Version
    private Long version;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        if (this.status == null) {
            this.status = BookingStatus.PENDING;
        }
    }
}
