package by.beltam.practice.client;

import by.beltam.practice.common.dto.catalog.RoomTypeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "catalog-service",
        configuration = FeignClientConfig.class
)
public interface CatalogClient {

    @GetMapping("/api/v1/catalog/room-types/{id}")
    RoomTypeDTO getRoomType(@PathVariable UUID id);
}
