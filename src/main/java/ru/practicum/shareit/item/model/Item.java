package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Item {
    @NotNull
    private final long id;
    private final String name;
    @NotBlank
    private final String description;
    private final boolean available;
    private final User owner;
}
