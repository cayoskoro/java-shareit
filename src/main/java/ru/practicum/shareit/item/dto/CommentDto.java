package ru.practicum.shareit.item.dto;

import lombok.*;

import java.time.Instant;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class CommentDto {
    private final Long id;
    private final String text;
    private final String authorName;
    private final Instant created;
}
