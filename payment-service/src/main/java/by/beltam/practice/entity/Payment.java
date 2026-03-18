package by.beltam.practice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID bookingId;
    private UUID userId;
    private BigDecimal cost;
    private String status;
    private String transactionId;

    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}