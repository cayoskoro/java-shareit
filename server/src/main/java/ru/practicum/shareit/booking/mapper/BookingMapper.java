package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.control.DeepClone;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)
public interface BookingMapper {
    @Mapping(target = "status", constant = "WAITING")
    Booking convertRequestDtoToEntity(BookingRequestDto dto);

    BookingResponseDto convertToResponseDto(Booking entity);

    @Mapping(target = "bookerId", source = "booker.id")
    BookingResponseShortDto convertToResponseShortDto(Booking entity);

    Collection<BookingResponseDto> convertToResponseDtoCollection(Collection<Booking> entities);
}
