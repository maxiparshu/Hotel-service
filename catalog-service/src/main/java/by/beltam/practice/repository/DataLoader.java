package by.beltam.practice.repository;

import by.beltam.practice.entity.Amenity;
import by.beltam.practice.entity.Hotel;
import by.beltam.practice.entity.RoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    public void run(String... args) throws Exception {

        Hotel hotel1 = new Hotel(UUID.randomUUID(), "Grand Resort", "Sea Ave 1", "Batumi",
                5.0, new ArrayList<>());
        Hotel hotel2 = new Hotel(UUID.randomUUID(), "Mountain View Inn", "Hill Road 12", "Gudauri",
                4.5, new ArrayList<>());

        hotelRepository.saveAll(Set.of(hotel1, hotel2));
        Amenity wifi = new Amenity(UUID.randomUUID(), "Free WiFi", "Tech");
        Amenity pool = new Amenity(UUID.randomUUID(), "Swimming Pool", "Leisure");
        Amenity breakfast = new Amenity(UUID.randomUUID(), "Breakfast Included", "Food");

        amenityRepository.saveAll(Set.of(wifi, pool, breakfast));

        RoomType deluxe = new RoomType(UUID.randomUUID(), "Deluxe Sea View", BigDecimal.valueOf(150.00),
                2, 5, hotel1, Set.of(wifi, pool));

        RoomType suite = new RoomType(UUID.randomUUID(), "Mountain Suite", BigDecimal.valueOf(200.00),
                3, 6, hotel2, Set.of(wifi, breakfast));

        roomTypeRepository.saveAll(Set.of(deluxe, suite));

    }
}