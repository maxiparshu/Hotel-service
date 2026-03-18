package by.beltam.practice.dto.hotel;

public record HotelSearchCriteria(
        String title,
        String address,
        String city,
        Double minRating,
        Double maxRating,
        String sortBy,
        String direction
) {
}
