package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.Collection;

@Value
@Builder(toBuilder = true)
public class ItemResponseDto {
    private final Long id;
    private final String name;
    private final String description;
    private final Boolean available;
    private final BookingResponseShortDto lastBooking;
    private final BookingResponseShortDto nextBooking;
    private final Collection<CommentDto> comments;
}
