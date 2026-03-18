package by.beltam.practice.common.dto.catalog;

import java.util.UUID;

public record HotelDTO(
        UUID id,
        String title,
        String city,
        String address,
        Double rating
) {
}
