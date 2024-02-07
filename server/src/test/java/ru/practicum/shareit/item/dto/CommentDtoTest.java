package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.ItemBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class CommentDtoTest extends ItemBaseTest {
    private final JacksonTester<CommentDto> json;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetCommentDto() throws Exception {

        log.info("commentDto = {}", commentDto1);
        JsonContent<CommentDto> result = json.write(commentDto1);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(commentDto1.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(
                commentDto1.getAuthorName());
    }
}