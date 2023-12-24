package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Item {
    @NotNull
    private long id;
    private String name;
    @NotBlank
    private String description;
    private boolean available;
    private User owner;
}
