package by.beltam.practice.repository;

import by.beltam.practice.entity.Amenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AmenityRepository extends JpaRepository<Amenity, UUID> {

    List<Amenity> findByCategory(String category);

    boolean existsAllByNameAndCategory(String name, String category);
}
