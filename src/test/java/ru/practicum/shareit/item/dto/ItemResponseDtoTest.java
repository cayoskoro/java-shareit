package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.item.ItemBaseTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class ItemResponseDtoTest extends ItemBaseTest {
    private final JacksonTester<ItemResponseDto> json;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetItemResponseDtoTest() throws Exception {

        log.info("itemResponseDto = {}", itemResponseDto1);
        JsonContent<ItemResponseDto> result = json.write(itemResponseDto1);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(
                itemResponseDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(itemResponseDto1.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(
                itemResponseDto1.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(
                itemResponseDto1.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id").isEqualTo(
                itemResponseDto1.getLastBooking().getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.bookerId").isEqualTo(
                itemResponseDto1.getLastBooking().getBookerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.id").isEqualTo(
                itemResponseDto1.getNextBooking().getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.nextBooking.bookerId").isEqualTo(
                itemResponseDto1.getNextBooking().getBookerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.comments[0].id").isEqualTo(
                commentDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].text").isEqualTo(
                commentDto1.getText());
        assertThat(result).extractingJsonPathStringValue("$.comments[0].authorName").isEqualTo(
                commentDto1.getAuthorName());
        assertThat(result).extractingJsonPathNumberValue("$.requestId").isEqualTo(
                itemResponseDto1.getRequestId().intValue());
    }
}