package by.beltam.practice.service;

import by.beltam.practice.dto.amenity.AmenityUpdateRequest;
import by.beltam.practice.dto.hotel.HotelSearchCriteria;
import by.beltam.practice.dto.hotel.HotelUpdateRequest;
import by.beltam.practice.dto.room_type.RoomTypeSearchCriteria;
import by.beltam.practice.dto.room_type.RoomTypeUpdateRequest;
import by.beltam.practice.entity.Amenity;
import by.beltam.practice.entity.Hotel;
import by.beltam.practice.entity.RoomType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CatalogService {

    List<RoomType> getRoomTypeAtHotel(UUID hotelId);
    RoomType getRoomType(UUID id);
    List<RoomType> filterRoomTypes(RoomTypeSearchCriteria criteria);


    RoomType createRoomType(RoomType roomType, UUID hotelId);

    RoomType updateRoomType(UUID roomId, RoomTypeUpdateRequest roomTypeUpdateRequest);

    void addAmenitiesToRoomType(UUID roomId,List<UUID> amenityId);

    void removeAmenitiesFromRoomType(UUID roomId,List <UUID> amenityId);

    void deleteRoomType(UUID roomId);

    // Hotel
    Hotel createHotel(Hotel hotel);
    Hotel getHotel(UUID id);

    List<Hotel> filterHotels(HotelSearchCriteria criteria);

    Hotel updateHotel(UUID hotelId, HotelUpdateRequest hotelUpdateRequest);

    List<String> getAvailableCities();

    void deleteHotel(UUID hotelId);

    // Amenity
    Amenity getAmenity(UUID id);
    List<Amenity> getAmenities();
    Amenity createAmenity(Amenity amenity);

    Amenity updateAmenity(UUID amenityId, AmenityUpdateRequest amenityUpdateRequest);

    void deleteAmenity(UUID amenityId);


}
