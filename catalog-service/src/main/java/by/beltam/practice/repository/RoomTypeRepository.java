package by.beltam.practice.repository;

import by.beltam.practice.entity.RoomType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, UUID>, JpaSpecificationExecutor<RoomType> {
    List<RoomType> findRoomTypeByHotel_Id(UUID hotelId, Sort sort);

    @Modifying
    @Transactional
    @Query(
            nativeQuery = true,
            value = "DELETE FROM ROOM_TYPE_AMENITIES WHERE AMENITY_ID = :amenityId"
    )
    void deleteAmenityFromRoom(@Param("amenityId") UUID amenityId);

    boolean existsRoomTypeByNameAndHotel_Id(String name, UUID hotel_id);
}
