package by.beltam.practice.dto.room_type;

import java.math.BigDecimal;
import java.util.UUID;

public record RoomTypeSearchCriteria (
        UUID hotelId,
        String name,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Integer capacity,
        String amenityCategory,

        String sortBy,
        String direction

){
}
