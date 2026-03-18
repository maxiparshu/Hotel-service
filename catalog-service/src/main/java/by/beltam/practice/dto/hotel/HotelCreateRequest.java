package by.beltam.practice.dto.hotel;

import jakarta.validation.constraints.*;

public record HotelCreateRequest(
        @NotBlank
        @Size(max = 255)
        String title,
        @NotBlank
        @Size(max = 55)
        String city,
        @NotBlank
        @Size(max = 255)
        String address,
        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("5.0")
        Double rating
) {
}
