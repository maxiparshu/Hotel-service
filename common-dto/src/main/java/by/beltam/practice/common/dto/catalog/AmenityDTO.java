package by.beltam.practice.common.dto.catalog;

import java.util.UUID;

public record AmenityDTO(
        UUID id,
        String name,
        String category
) { }
