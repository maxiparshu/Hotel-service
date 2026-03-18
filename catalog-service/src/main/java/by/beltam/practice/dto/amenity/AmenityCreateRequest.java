package by.beltam.practice.dto.amenity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AmenityCreateRequest (
    @NotBlank
    @Size(max = 255)
    String name,
    @NotBlank
    @Size(max = 255)
    String category
){}
