package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class BookingDto {
    private final Long id;
    private final Instant start;
    private final Instant end;
    private final Item item;
    private final User booker;
    @NotNull
    private final Status status;
}
