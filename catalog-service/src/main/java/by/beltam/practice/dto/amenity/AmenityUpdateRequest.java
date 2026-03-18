package by.beltam.practice.dto.amenity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AmenityUpdateRequest(
        @Size(max = 255)
        String name,
        @Size(max = 255)
        String category
) {
}