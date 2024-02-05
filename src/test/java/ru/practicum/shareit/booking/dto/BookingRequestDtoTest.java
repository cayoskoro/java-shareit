package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class BookingRequestDtoTest {
    private final JacksonTester<BookingRequestDto> json;

    @Test
    void shouldGetBookingRequestDto() throws Exception {
        BookingRequestDto bookingRequestDto = BookingRequestDto.builder()
                .itemId(1L)
                .start(LocalDateTime.of(2024, 1, 1, 12, 0))
                .end(LocalDateTime.of(2024, 1, 1, 13, 0))
                .build();
        log.info("bookingDto = {}", bookingRequestDto);
        JsonContent<BookingRequestDto> result = json.write(bookingRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.itemId").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start").isEqualTo("2024-01-01T12:00:00");
        assertThat(result).extractingJsonPathStringValue("$.end").isEqualTo("2024-01-01T13:00:00");
    }
}