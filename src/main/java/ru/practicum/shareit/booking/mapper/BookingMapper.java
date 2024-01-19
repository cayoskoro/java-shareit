package ru.practicum.shareit.booking.mapper;

import org.mapstruct.*;
import org.mapstruct.control.DeepClone;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

@Mapper(componentModel = "spring", mappingControl = DeepClone.class)
public interface BookingMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void updateBookingFromBookingDto(BookingDto dto, @MappingTarget Booking booking);

    Booking convertToEntity(BookingDto dto);

    BookingDto convertToDto(Booking entity);

    Booking clone(Booking entity);
}
