package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class BookingResponseShortDtoTest {
    private final JacksonTester<BookingResponseShortDto> json;

    @Test
    void shouldGetBookingResponseShortDto() throws Exception {
        BookingResponseShortDto bookingResponseShortDto = BookingResponseShortDto.builder()
                .id(1L)
                .bookerId(1L)
                .build();
        log.info("bookingDto = {}", bookingResponseShortDto);
        JsonContent<BookingResponseShortDto> result = json.write(bookingResponseShortDto);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.bookerId").isEqualTo(1);

    }
}