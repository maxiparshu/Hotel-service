package by.beltam.practice.common.dto.catalog;


import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record RoomTypeDTO(
        UUID id,
        String name,
        BigDecimal basePrice,
        Integer capacity,
        UUID hotelID,
        Integer totalRooms,
        Set<AmenityDTO> amenities
) {
}
