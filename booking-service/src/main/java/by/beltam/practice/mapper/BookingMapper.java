package by.beltam.practice.mapper;

import by.beltam.practice.dto.BookingResponse;
import by.beltam.practice.entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponse toResponse(Booking booking);
    List<BookingResponse> toResponseList(List<Booking> bookings);
}