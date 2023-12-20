package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
public class ItemDto {
    @NotBlank
    private final String description;
    private final boolean available;
}
