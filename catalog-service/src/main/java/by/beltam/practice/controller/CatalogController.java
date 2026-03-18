package by.beltam.practice.controller;

import by.beltam.practice.common.dto.catalog.AmenityDTO;
import by.beltam.practice.common.dto.catalog.HotelDTO;
import by.beltam.practice.common.dto.catalog.RoomTypeDTO;
import by.beltam.practice.dto.amenity.AmenityCreateRequest;
import by.beltam.practice.dto.amenity.AmenityUpdateRequest;
import by.beltam.practice.dto.hotel.HotelCreateRequest;
import by.beltam.practice.dto.hotel.HotelSearchCriteria;
import by.beltam.practice.dto.hotel.HotelUpdateRequest;
import by.beltam.practice.dto.room_type.RoomTypeCreateRequest;
import by.beltam.practice.dto.room_type.RoomTypeSearchCriteria;
import by.beltam.practice.dto.room_type.RoomTypeUpdateRequest;
import by.beltam.practice.mapper.CatalogMapper;
import by.beltam.practice.service.CatalogService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
@Validated
public class CatalogController {

    private final CatalogService catalogService;
    private final CatalogMapper mapper;


    @GetMapping("/hotels")
    public List<HotelDTO> readHotels(HotelSearchCriteria criteria) {
        return mapper.toHotelDtoList(catalogService.filterHotels(criteria));
    }

    @GetMapping("/hotels/{id}")
    public HotelDTO getHotel(@PathVariable UUID id) {
        return mapper.toHotelDto(catalogService.getHotel(id));
    }

    @GetMapping("/cities")
    public List<String> readHotels() {
        return catalogService.getAvailableCities();
    }

    @PostMapping("/hotels")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelDTO createHotel(@Valid @RequestBody HotelCreateRequest request) {
        return mapper.toHotelDto(
                catalogService.createHotel(mapper.toHotel(request))
        );
    }

    @PatchMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public HotelDTO updateHotel(@PathVariable UUID id,
                                @Valid @RequestBody HotelUpdateRequest request) {
        return mapper.toHotelDto(catalogService.updateHotel(id, request));
    }

    @DeleteMapping("/hotels/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHotel(@PathVariable UUID id) {
        catalogService.deleteHotel(id);
    }

    @GetMapping("/room-types")
    public List<RoomTypeDTO> readRoomTypes(RoomTypeSearchCriteria criteria) {
        return mapper.toRoomTypeDtoList(catalogService.filterRoomTypes(criteria));
    }

    @GetMapping("/room-types/{id}")
    public RoomTypeDTO getRoomType(@PathVariable UUID id) {
        return mapper.toRoomTypeDto(catalogService.getRoomType(id));
    }

    @PostMapping("/hotels/{hotelId}/room-types")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public RoomTypeDTO createRoomType(@NotNull @PathVariable UUID hotelId,
                                      @Valid @RequestBody RoomTypeCreateRequest request) {
        return mapper.toRoomTypeDto(
                catalogService.createRoomType(mapper.toRoomType(request), hotelId)
        );
    }

    @PatchMapping("/room-types/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RoomTypeDTO updateRoomType(@PathVariable UUID id,
                                      @Valid @RequestBody RoomTypeUpdateRequest request) {
        return mapper.toRoomTypeDto(catalogService.updateRoomType(id, request));
    }

    @DeleteMapping("/room-types/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoomType(@PathVariable UUID id) {
        catalogService.deleteRoomType(id);
    }


    @GetMapping("/amenities")
    public List<AmenityDTO> readAmenities() {
        return mapper.toAmenityDtoList(catalogService.getAmenities());
    }

    @GetMapping("/amenities/{id}")
    public AmenityDTO getAmenityById(@PathVariable UUID id) {
        return mapper.toAmenityDto(catalogService.getAmenity(id));
    }

    @PostMapping("/amenities")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public AmenityDTO createAmenity(@Valid @RequestBody AmenityCreateRequest request) {
        return mapper.toAmenityDto(
                catalogService.createAmenity(mapper.toAmenity(request))
        );
    }

    @PatchMapping("/amenities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public AmenityDTO updateAmenity(@PathVariable UUID id,
                                    @Valid @RequestBody AmenityUpdateRequest request) {
        return mapper.toAmenityDto(catalogService.updateAmenity(id, request));
    }

    @DeleteMapping("/amenities/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAmenity(@PathVariable UUID id) {
        catalogService.deleteAmenity(id);
    }


    @PatchMapping("/room-types/{id}/amenities")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addAmenities(@PathVariable UUID id,
                             @RequestBody List<UUID> amenitiesIds) {
        catalogService.addAmenitiesToRoomType(id, amenitiesIds);
    }

    @DeleteMapping("/room-types/{id}/amenities")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void removeAmenities(@PathVariable UUID id,
                                @RequestBody List<UUID> amenitiesIds) {
        catalogService.removeAmenitiesFromRoomType(id, amenitiesIds);
    }
}