package by.beltam.practice.dto.room_type;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record RoomTypeUpdateRequest(
        @Size(max = 255)
        String name,

        @Positive
        BigDecimal basePrice,
        @Positive
        Integer capacity,
        @Positive
        Integer totalCount
) {
}
