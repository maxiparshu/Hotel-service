package by.beltam.practice.repository;

import by.beltam.practice.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, UUID>, JpaSpecificationExecutor<Hotel> {
    @Query("select distinct h.city from Hotel h order by h.city")
    List<String> findDistinctCities();

    boolean existsHotelByAddressAndCity(String address, String city);
}
