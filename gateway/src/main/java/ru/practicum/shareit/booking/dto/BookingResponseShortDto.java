package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class BookingResponseShortDto {
    private final Long id;
    private final Long bookerId;
}
