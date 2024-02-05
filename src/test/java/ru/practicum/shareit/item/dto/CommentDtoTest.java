package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class CommentDtoTest {
    private final JacksonTester<CommentDto> json;

    @Test
    void shouldGetCommentDto() throws Exception {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("item1")
                .authorName("user1")
                .created(LocalDateTime.now())
                .build();

        log.info("commentDto = {}", commentDto);
        JsonContent<CommentDto> result = json.write(commentDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(
                commentDto.getAuthorName());
    }
}