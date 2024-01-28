package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Value
@Builder(toBuilder = true)
public class CommentDto {
    private final Long id;
    @NotBlank
    private final String text;
    private final String authorName;
    private final Instant created;
}
