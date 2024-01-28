package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)
public interface BookingMapper {
    @Mapping(target = "status", constant = "WAITING")
    Booking convertRequestDtoToEntity(BookingRequestDto dto);

    BookingResponseDto convertToResponseDto(Booking entity);
}
