package by.beltam.practice.common.dto.payment;

import java.util.UUID;

public record PaymentResultEvent(
        UUID bookingId,
        boolean success,
        String transactionId,
        String message
) {
}
