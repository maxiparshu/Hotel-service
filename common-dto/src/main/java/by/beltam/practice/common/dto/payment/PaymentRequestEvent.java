package by.beltam.practice.common.dto.payment;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentRequestEvent(
        UUID bookingId,
        UUID userId,
        @JsonDeserialize(using = NumberDeserializers.BigDecimalDeserializer.class)
        BigDecimal cost,
        String currency
) {
}
