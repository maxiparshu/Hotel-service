package by.beltam.practice.specification;

import by.beltam.practice.dto.room_type.RoomTypeSearchCriteria;
import by.beltam.practice.entity.*;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.UUID;

public class RoomTypeSpecification {
    private static Specification<RoomType> byHotel(UUID hotelId) {
        return (root, query, criteriaBuilder) -> {
            if (hotelId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(RoomType_.hotel).get(Hotel_.id), hotelId);
        };
    }

    private static Specification<RoomType> betweenPrice(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return criteriaBuilder.conjunction();
            }
            Path<BigDecimal> path = root.get(RoomType_.basePrice);
            if (minPrice == null) {
                return criteriaBuilder.lessThanOrEqualTo(path, maxPrice);
            }
            if (maxPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(path, minPrice);
            }
            return criteriaBuilder.between(path, minPrice, maxPrice);
        };
    }

    private static Specification<RoomType> containName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(name)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(RoomType_.name)),
                    "%" + name.toLowerCase() + "%");
        };
    }

    private static Specification<RoomType> byCapacity(Integer capacity) {
        return (root, query, criteriaBuilder) -> {
            if (capacity == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get(RoomType_.capacity), capacity);
        };
    }

    private static Specification<RoomType> byAmenityCategory(String amenityCategory) {
        return (root, query, cb) -> {

            if (!StringUtils.hasText(amenityCategory)) {
                return cb.conjunction();
            }

            Join<RoomType, Amenity> amenitiesJoin = root.join(RoomType_.amenities);
            query.distinct(true);
            return cb.equal(amenitiesJoin.get(Amenity_.category), amenityCategory);
        };
    }

    public static Specification<RoomType> fromCriteria(RoomTypeSearchCriteria criteria) {
        return Specification
                .where(RoomTypeSpecification.byHotel(criteria.hotelId()))
                .and(RoomTypeSpecification.betweenPrice(criteria.minPrice(), criteria.maxPrice()))
                .and(RoomTypeSpecification.byCapacity(criteria.capacity()))
                .and(RoomTypeSpecification.byAmenityCategory(criteria.amenityCategory()))
                .and(RoomTypeSpecification.containName(criteria.name()));

    }
}
