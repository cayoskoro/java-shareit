package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;

    @Test
    void shouldGetItemRequestDtoTest() throws Exception {
        BookingResponseShortDto bookingResponseShortDto1 = BookingResponseShortDto.builder()
                .id(1L)
                .bookerId(1L)
                .build();

        BookingResponseShortDto bookingResponseShortDto2 = BookingResponseShortDto.builder()
                .id(2L)
                .bookerId(2L)
                .build();

        CommentDto commentDto1 = CommentDto.builder()
                .id(1L)
                .text("item1")
                .authorName("user1")
                .created(LocalDateTime.now())
                .build();

        CommentDto commentDto2 = CommentDto.builder()
                .id(1L)
                .text("item2")
                .authorName("user2")
                .created(LocalDateTime.now())
                .build();

        ItemResponseDto itemResponseDto = ItemResponseDto.builder()
                .id(1L)
                .name("item1")
                .description("item1")
                .available(true)
                .lastBooking(bookingResponseShortDto1)
                .nextBooking(bookingResponseShortDto2)
                .comments(List.of(commentDto1, commentDto2))
                .requestId(1L)
                .build();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("itemRequest1")
                .created(LocalDateTime.now())
                .items(List.of(itemResponseDto))
                .build();

        log.info("itemRequestDto = {}", itemRequestDto);
        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(
                itemRequestDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(
                itemRequestDto.getDescription());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id").isEqualTo(
                itemResponseDto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].name").isEqualTo(
                itemResponseDto.getName());
        assertThat(result).extractingJsonPathStringValue("$.items[0].description").isEqualTo(
                itemResponseDto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available").isEqualTo(
                itemResponseDto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].lastBooking.id").isEqualTo(
                itemResponseDto.getLastBooking().getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].lastBooking.bookerId").isEqualTo(
                itemResponseDto.getLastBooking().getBookerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].nextBooking.id").isEqualTo(
                itemResponseDto.getNextBooking().getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].nextBooking.bookerId").isEqualTo(
                itemResponseDto.getNextBooking().getBookerId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].comments[0].id").isEqualTo(
                commentDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments[0].text").isEqualTo(
                commentDto1.getText());
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments[0].authorName").isEqualTo(
                commentDto1.getAuthorName());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].comments[1].id").isEqualTo(
                commentDto2.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments[1].text").isEqualTo(
                commentDto2.getText());
        assertThat(result).extractingJsonPathStringValue("$.items[0].comments[1].authorName").isEqualTo(
                commentDto2.getAuthorName());
        assertThat(result).extractingJsonPathNumberValue("$.items[0].requestId").isEqualTo(
                itemResponseDto.getRequestId().intValue());
    }
}