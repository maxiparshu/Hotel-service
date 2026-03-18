package by.beltam.practice.specification;

import by.beltam.practice.dto.hotel.HotelSearchCriteria;
import by.beltam.practice.entity.Hotel;
import by.beltam.practice.entity.Hotel_;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public class HotelSpecification {

    private static Specification<Hotel> containTitle(String title) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(title)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Hotel_.title)),
                    "%" + title.toLowerCase() + "%");
        };
    }

    private static Specification<Hotel> byCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(city)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(Hotel_.city), city);
        };
    }

    private static Specification<Hotel> containAddress(String address) {
        return (root, query, criteriaBuilder) -> {
            if (!StringUtils.hasText(address)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get(Hotel_.address)),
                    "%" + address.toLowerCase() + "%");
        };
    }

    private static Specification<Hotel> betweenRating(Double minRating, Double maxRating) {
        return (root, query, criteriaBuilder) -> {
            if (minRating == null && maxRating == null) {
                return criteriaBuilder.conjunction();
            }
            Path<Double> path = root.get(Hotel_.rating);
            if (minRating == null) {
                return criteriaBuilder.lessThanOrEqualTo(path, maxRating);
            }
            if (maxRating == null) {
                return criteriaBuilder.greaterThanOrEqualTo(path, minRating);
            }
            return criteriaBuilder.between(path, minRating, maxRating);
        };
    }

    public static Specification<Hotel> fromCriteria(HotelSearchCriteria criteria) {
        return Specification
                .where(HotelSpecification.containTitle(criteria.title()))
                .and(HotelSpecification.betweenRating(criteria.minRating(), criteria.maxRating()))
                .and(HotelSpecification.containAddress(criteria.address()))
                .and(HotelSpecification.byCity(criteria.city()));
    }
}
