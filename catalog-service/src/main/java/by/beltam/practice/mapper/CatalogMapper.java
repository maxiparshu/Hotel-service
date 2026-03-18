package by.beltam.practice.mapper;

import by.beltam.practice.common.dto.catalog.AmenityDTO;
import by.beltam.practice.common.dto.catalog.HotelDTO;
import by.beltam.practice.common.dto.catalog.RoomTypeDTO;
import by.beltam.practice.dto.amenity.AmenityCreateRequest;
import by.beltam.practice.dto.hotel.HotelCreateRequest;
import by.beltam.practice.dto.room_type.RoomTypeCreateRequest;
import by.beltam.practice.entity.Amenity;
import by.beltam.practice.entity.Hotel;
import by.beltam.practice.entity.RoomType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring",
        imports = {java.util.HashSet.class})
public interface CatalogMapper {
    HotelDTO toHotelDto(Hotel hotel);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roomTypes", ignore = true)
    Hotel toHotel(HotelCreateRequest hotelDto);


    List<HotelDTO> toHotelDtoList(List<Hotel> hotels);

    @Mapping(target = "hotelID", source = "hotel.id")
    RoomTypeDTO toRoomTypeDto(RoomType roomType);

    @Mapping(target = "amenities", expression = "java(new HashSet<>())")
    @Mapping(target = "hotel", ignore = true)
    @Mapping(target = "id", ignore = true)
    RoomType toRoomType(RoomTypeCreateRequest roomTypeDTO);

    @Mapping(target = "id", ignore = true)
    List<RoomTypeDTO> toRoomTypeDtoList(List<RoomType> roomTypes);

    @Mapping(target = "id", ignore = true)
    AmenityDTO toAmenityDto(Amenity amenity);

    @Mapping(target = "id", ignore = true)
    Amenity toAmenity(AmenityCreateRequest amenityDTO);


    List<AmenityDTO> toAmenityDtoList(List<Amenity> amenities);

    Set<AmenityDTO> toAmenityDtoSet(Set<Amenity> amenities);
}
