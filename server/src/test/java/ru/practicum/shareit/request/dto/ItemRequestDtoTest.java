package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.ItemRequestBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class ItemRequestDtoTest extends ItemRequestBaseTest {
    private final JacksonTester<ItemRequestDto> json;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetItemRequestDtoTest() throws Exception {

        log.info("itemRequestDto1 = {}", itemRequestDto1);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto1);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(
                itemRequestDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(
                itemRequestDto1.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(
                itemResponseDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(
                itemResponseDto1.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(
                itemResponseDto1.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(
                itemResponseDto1.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].lastBooking.id").isEqualTo(
                itemResponseDto1.getLastBooking().getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].lastBooking.bookerId").isEqualTo(
                itemResponseDto1.getLastBooking().getBookerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].nextBooking.id").isEqualTo(
                itemResponseDto1.getNextBooking().getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].nextBooking.bookerId").isEqualTo(
                itemResponseDto1.getNextBooking().getBookerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].comments[0].id").isEqualTo(
                commentDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments[0].text").isEqualTo(
                commentDto1.getText());
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments[0].authorName").isEqualTo(
                commentDto1.getAuthorName());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(
                itemResponseDto1.getRequestId().intValue());
    }
}