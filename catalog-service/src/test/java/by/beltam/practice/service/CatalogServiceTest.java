package by.beltam.practice.service;

import by.beltam.practice.dto.amenity.AmenityUpdateRequest;
import by.beltam.practice.dto.hotel.HotelUpdateRequest;
import by.beltam.practice.dto.room_type.RoomTypeUpdateRequest;
import by.beltam.practice.entity.Amenity;
import by.beltam.practice.entity.Hotel;
import by.beltam.practice.entity.RoomType;
import by.beltam.practice.exception.NotFoundResourceException;
import by.beltam.practice.repository.AmenityRepository;
import by.beltam.practice.repository.HotelRepository;
import by.beltam.practice.repository.RoomTypeRepository;
import by.beltam.practice.service.impl.CatalogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CatalogServiceImplTest {

    private RoomTypeRepository roomTypeRepository;
    private HotelRepository hotelRepository;
    private AmenityRepository amenityRepository;
    private CatalogServiceImpl catalogService;

    @BeforeEach
    void setup() {
        roomTypeRepository = mock(RoomTypeRepository.class);
        hotelRepository = mock(HotelRepository.class);
        amenityRepository = mock(AmenityRepository.class);
        catalogService = new CatalogServiceImpl(roomTypeRepository, hotelRepository, amenityRepository);
    }

    @Test
    void createRoomTypeSuccess() {
        Hotel hotel = new Hotel();
        UUID hotelId = UUID.randomUUID();
        hotel.setId(hotelId);
        RoomType roomType = new RoomType();
        roomType.setName("Deluxe");

        when(hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));
        when(roomTypeRepository.save(any())).thenAnswer(
                invocationOnMock -> invocationOnMock.getArgument(0)
        );

        RoomType result = catalogService.createRoomType(roomType, hotel.getId());

        assertEquals(result.getHotel().getId(), hotelId);
        verify(roomTypeRepository).save(roomType);
    }

    @Test
    void createRoomTypeHotelNotFound() {
        UUID hotelId = UUID.randomUUID();
        RoomType roomType = new RoomType();
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        assertThrows(NotFoundResourceException.class, () ->
                catalogService.createRoomType(roomType, hotelId));
    }

    @Test
    void createHotelSuccess() {
        Hotel hotel = new Hotel();
        String title = "Test hotel";
        hotel.setTitle(title);
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        Hotel result = catalogService.createHotel(hotel);

        assertEquals(result.getTitle(), title);
        verify(hotelRepository).save(hotel);
    }

    @Test
    void createAmenitySuccess() {
        Amenity amenity = new Amenity();
        String name = "Wi-Fi";
        amenity.setName(name);
        when(amenityRepository.save(amenity)).thenReturn(amenity);

        Amenity result = catalogService.createAmenity(amenity);

        assertEquals(result.getName(), name);
        verify(amenityRepository).save(amenity);
    }

    @Test
    void updateRoomTypeSuccess() {
        UUID roomId = UUID.randomUUID();
        RoomType roomType = new RoomType();
        roomType.setAmenities(new HashSet<>());
        when(roomTypeRepository.findById(roomId)).thenReturn(Optional.of(roomType));

        String name = "Deluxe";
        BigDecimal price = BigDecimal.valueOf(100);
        Integer capacity = 2;

        RoomTypeUpdateRequest req = new RoomTypeUpdateRequest(name, price, capacity);
        RoomType updated = catalogService.updateRoomType(roomId, req);

        assertEquals(name, updated.getName());
        assertEquals(price, updated.getBasePrice());
        assertEquals(capacity, updated.getCapacity());
    }

    @Test
    void updateHotelSuccess() {
        UUID hotelId = UUID.randomUUID();
        Hotel hotel = new Hotel();
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        String name = "\"Minsk\"";
        String city = "Minsk";
        String address = "Nezavisimosti 11";
        Double rating = 4.0;
        HotelUpdateRequest request = new HotelUpdateRequest(name, city, address, rating);
        Hotel updated = catalogService.updateHotel(hotelId, request);

        assertEquals(updated.getTitle(), name);
        assertEquals(updated.getCity(), city);
        assertEquals(updated.getAddress(), address);
        assertEquals(updated.getRating(), rating);
    }

    @Test
    void updateAmenitySuccess() {
        UUID amenityId = UUID.randomUUID();
        Amenity amenity = new Amenity();
        when(amenityRepository.findById(amenityId)).thenReturn(Optional.of(amenity));
        String name = "Minibar";
        String category = "White goods";
        AmenityUpdateRequest req = new AmenityUpdateRequest(name, category);
        Amenity updated = catalogService.updateAmenity(amenityId, req);

        assertEquals(updated.getName(), name);
        assertEquals(updated.getCategory(), category);
    }

    @Test
    void deleteRoomTypeSuccess() {
        RoomType roomType = new RoomType();
        roomType.setId(UUID.randomUUID());
        roomType.setAmenities(new HashSet<>());
        when(roomTypeRepository.findById(roomType.getId())).thenReturn(Optional.of(roomType));

        catalogService.deleteRoomType(roomType.getId());

        verify(roomTypeRepository).delete(roomType);
    }

    @Test
    void deleteRoomTypeNotFound() {
        UUID roomId = UUID.randomUUID();
        when(roomTypeRepository.findById(roomId)).thenReturn(Optional.empty());

        assertThrows(NotFoundResourceException.class, () -> catalogService.deleteRoomType(roomId));
    }

    @Test
    void deleteHotelSuccess() {
        Hotel hotel = new Hotel();
        hotel.setId(UUID.randomUUID());
        when(hotelRepository.findById(hotel.getId())).thenReturn(Optional.of(hotel));

        catalogService.deleteHotel(hotel.getId());

        verify(hotelRepository).delete(hotel);
    }

    @Test
    void deleteAmenitySuccess() {
        Amenity amenity = new Amenity();
        amenity.setId(UUID.randomUUID());
        when(amenityRepository.findById(amenity.getId())).thenReturn(Optional.of(amenity));

        catalogService.deleteAmenity(amenity.getId());

        verify(roomTypeRepository).deleteAmenityFromRoom(amenity.getId());
        verify(amenityRepository).delete(amenity);
    }

    @Test
    void addAmenityToRoomTypeSuccess() {
        UUID roomId = UUID.randomUUID();
        UUID amenityId = UUID.randomUUID();
        RoomType roomType = new RoomType();
        roomType.setAmenities(new HashSet<>());
        Amenity amenity = new Amenity();
        amenity.setId(amenityId);

        when(roomTypeRepository.findById(roomId)).thenReturn(Optional.of(roomType));
        when(amenityRepository.findById(amenityId)).thenReturn(Optional.of(amenity));

        catalogService.addAmenitiesToRoomType(roomId, List.of(amenityId));

        assertThat(roomType.getAmenities()).contains(amenity);
    }

    @Test
    void removeAmenityFromRoomTypeSuccess() {
        UUID roomId = UUID.randomUUID();
        UUID amenityId = UUID.randomUUID();
        Amenity amenity = new Amenity();
        amenity.setId(amenityId);

        RoomType roomType = new RoomType();
        roomType.setAmenities(new HashSet<>(Set.of(amenity)));

        when(roomTypeRepository.findById(roomId)).thenReturn(Optional.of(roomType));
        when(amenityRepository.findById(amenityId)).thenReturn(Optional.of(amenity));

        catalogService.removeAmenitiesFromRoomType(roomId, List.of(amenityId));
        assertThat(roomType.getAmenities()).doesNotContain(amenity);
    }

}
