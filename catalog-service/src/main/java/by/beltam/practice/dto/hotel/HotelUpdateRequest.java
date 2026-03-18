package by.beltam.practice.dto.hotel;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record HotelUpdateRequest(
        @Size(max = 255)
        String title,
        @Size(max = 55)
        String city,
        @Size(max = 255)
        String address,
        @DecimalMin("0.0")
        @DecimalMax("5.0")
        Double rating
) {
}
