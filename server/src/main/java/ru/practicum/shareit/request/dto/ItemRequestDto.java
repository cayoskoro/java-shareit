package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;

@Value
@Builder(toBuilder = true)
public class ItemRequestDto {
    private final Long id;
    @NotBlank
    private final String description;
    private final LocalDateTime created;
    private final Collection<ItemResponseDto> items;
}
