package by.beltam.practice.service.impl;

import by.beltam.practice.common.exception.ConflictException;
import by.beltam.practice.common.exception.NotFoundException;
import by.beltam.practice.dto.amenity.AmenityUpdateRequest;
import by.beltam.practice.dto.hotel.HotelSearchCriteria;
import by.beltam.practice.dto.hotel.HotelUpdateRequest;
import by.beltam.practice.dto.room_type.RoomTypeSearchCriteria;
import by.beltam.practice.dto.room_type.RoomTypeUpdateRequest;
import by.beltam.practice.entity.*;
import by.beltam.practice.repository.AmenityRepository;
import by.beltam.practice.repository.HotelRepository;
import by.beltam.practice.repository.RoomTypeRepository;
import by.beltam.practice.service.CatalogService;
import by.beltam.practice.specification.HotelSpecification;
import by.beltam.practice.specification.RoomTypeSpecification;
import by.beltam.practice.utility.SortUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    private final RoomTypeRepository roomTypeRepository;
    private final HotelRepository hotelRepository;
    private final AmenityRepository amenityRepository;

    private static final Set<String> ALLOWED_ROOM_TYPE_SORT_FIELDS = Set.of(
            RoomType_.NAME,
            RoomType_.BASE_PRICE,
            RoomType_.CAPACITY
    );
    private static final Set<String> ALLOWED_HOTEL_SORT_FIELDS = Set.of(
            Hotel_.TITLE,
            Hotel_.RATING
    );

    // create
    @Override
    public Hotel createHotel(Hotel hotel) {
        if (hotelRepository.existsHotelByAddressAndCity(hotel.getAddress(), hotel.getCity()))
            throw new ConflictException("hotel at this city and address already exist");
        return hotelRepository.save(hotel);

    }




    @Override
    public Amenity createAmenity(Amenity amenity) {
        if (amenityRepository.existsAllByNameAndCategory(amenity.getName(), amenity.getCategory())) {
            throw new ConflictException("amenity with this name and category already exist");
        }
        return amenityRepository.save(amenity);
    }

    @Override
    public RoomType createRoomType(RoomType roomType, UUID hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("hotel with this id"));
        if (roomTypeRepository.existsRoomTypeByNameAndHotel_Id(roomType.getName(), hotelId)){
            throw new ConflictException("room type at this hotel and name already exist");
        }
        roomType.setHotel(hotel);
        return roomTypeRepository.save(roomType);
    }

    // read
    @Override
    public Hotel getHotel(UUID id) {
        return hotelRepository.findById(id).orElseThrow(
                () -> new NotFoundException("hotel with this id")
        );
    }

    @Transactional
    @Override
    public List<RoomType> getRoomTypeAtHotel(UUID hotelId) {
        return roomTypeRepository.findRoomTypeByHotel_Id(hotelId, Sort.by("name"));
    }

    @Override
    @Transactional
    public RoomType getRoomType(UUID id) {
        return roomTypeRepository.findById(id).orElseThrow(
                () -> new NotFoundException("room with this id")
        );
    }

    @Override
    @Transactional
    public List<RoomType> filterRoomTypes(RoomTypeSearchCriteria criteria) {
        Specification<RoomType> spec = RoomTypeSpecification.fromCriteria(criteria);
        Sort sort = SortUtils.getSortFrom(
                ALLOWED_ROOM_TYPE_SORT_FIELDS,
                criteria.sortBy(), "name",
                criteria.direction());
        return roomTypeRepository.findAll(spec, sort);
    }

    @Override
    @Transactional
    public List<Hotel> filterHotels(HotelSearchCriteria criteria) {
        Specification<Hotel> filterSpecification = HotelSpecification.fromCriteria(criteria);
        Sort sort = SortUtils.getSortFrom(
                ALLOWED_HOTEL_SORT_FIELDS,
                criteria.sortBy(), "title",
                criteria.direction());
        return hotelRepository.findAll(filterSpecification, sort);
    }

    @Override
    public List<String> getAvailableCities() {
        return hotelRepository.findDistinctCities();
    }

    //update
    @Override
    public Hotel updateHotel(UUID hotelId, HotelUpdateRequest hotelUpdateRequest) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("hotel with this id"));
        String title = hotelUpdateRequest.title();
        String city = hotelUpdateRequest.city();
        String address = hotelUpdateRequest.address();
        Double rating = hotelUpdateRequest.rating();
        if (StringUtils.hasText(title)) {
            hotel.setTitle(title);
        }
        if (StringUtils.hasText(city)) {
            hotel.setCity(city);
        }
        if (StringUtils.hasText(address)) {
            hotel.setAddress(address);
        }
        if (rating != null) {
            hotel.setRating(rating);
        }
        return hotelRepository.save(hotel);
    }

    @Override
    public RoomType updateRoomType(UUID roomId, RoomTypeUpdateRequest roomTypeUpdateRequest) {
        RoomType roomType = roomTypeRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("room with this id"));
        String name = roomTypeUpdateRequest.name();
        BigDecimal basePrice = roomTypeUpdateRequest.basePrice();
        Integer capacity = roomTypeUpdateRequest.capacity();
        Integer totalCount = roomTypeUpdateRequest.totalCount();

        if (StringUtils.hasText(name)) {
            roomType.setName(name);
        }
        if (basePrice != null) {
            roomType.setBasePrice(basePrice);
        }
        if (capacity != null) {
            roomType.setCapacity(capacity);
        }
        if (totalCount != null) {
            roomType.setCapacity(capacity);
        }
        return roomTypeRepository.save(roomType);
    }

    @Override
    @Transactional
    public void addAmenitiesToRoomType(UUID roomId, List<UUID> amenitiesId) {
        RoomType roomType = roomTypeRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("room with this id"));
        for (UUID amenityId : amenitiesId) {
            Amenity amenity = amenityRepository.findById(amenityId)
                    .orElseThrow(() -> new NotFoundException("amenity with this id"));
            roomType.getAmenities().add(amenity);
        }
    }

    @Transactional
    @Override
    public void removeAmenitiesFromRoomType(UUID roomId, List<UUID> amenitiesId) {
        RoomType roomType = roomTypeRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("room with this id"));
        for (UUID amenityId : amenitiesId) {
            Amenity amenity = amenityRepository.findById(amenityId)
                    .orElseThrow(() -> new NotFoundException("amenity with this id"));
            roomType.getAmenities().remove(amenity);
        }
    }

    @Override
    public Amenity updateAmenity(UUID amenityId, AmenityUpdateRequest amenityUpdateRequest) {
        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new NotFoundException("amenity with this id"));
        String name = amenityUpdateRequest.name();
        String category = amenityUpdateRequest.category();
        if (StringUtils.hasText(name)) {
            amenity.setName(name);
        }
        if (StringUtils.hasText(category)) {
            amenity.setCategory(category);
        }
        return amenityRepository.save(amenity);
    }

    //delete
    @Override
    @Transactional
    public void deleteAmenity(UUID amenityId) {
        Amenity amenity = amenityRepository.findById(amenityId)
                .orElseThrow(() -> new NotFoundException("amenity with this id"));
        roomTypeRepository.deleteAmenityFromRoom(amenityId);
        amenityRepository.delete(amenity);
    }

    @Override
    @Transactional
    public void deleteRoomType(UUID roomId) {
        RoomType roomType = roomTypeRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException("room with this id"));
        roomType.getAmenities().clear();
        roomTypeRepository.delete(roomType);
    }

    @Override
    public void deleteHotel(UUID hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new NotFoundException("hotel with this id"));
        hotelRepository.delete(hotel);
    }

    @Override
    public Amenity getAmenity(UUID id) {
        return amenityRepository.findById(id).orElseThrow(
                () -> new NotFoundException("amenity with this id")
        );
    }

    @Override
    public List<Amenity> getAmenities() {
        return amenityRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

}
