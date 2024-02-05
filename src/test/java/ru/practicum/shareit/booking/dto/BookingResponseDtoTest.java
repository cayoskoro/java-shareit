package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class BookingResponseDtoTest {
    private final JacksonTester<BookingResponseDto> json;

    @Test
    void shouldGetBookingResponseDto() throws Exception {
        Item item = Item.builder()
                .id(1L)
                .name("item")
                .description("item1")
                .available(true)
                .build();

        User user = User.builder()
                .id(1L)
                .name("user")
                .email("user@ya.ru")
                .build();

        BookingResponseDto bookingResponseDto = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.of(2024, 1, 1, 12, 0))
                .end(LocalDateTime.of(2024, 1, 1, 13, 0))
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();
        log.info("bookingDto = {}", bookingResponseDto);
        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-01-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-01-01T13:00:00");
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(item.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo(item.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item.isAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(user.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(user.getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(user.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");

    }
}