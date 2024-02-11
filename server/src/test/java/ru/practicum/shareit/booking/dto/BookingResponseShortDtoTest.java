package ru.practicum.shareit.booking.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.BookingBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class BookingResponseShortDtoTest extends BookingBaseTest {
    private final JacksonTester<BookingResponseShortDto> json;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetBookingResponseShortDto() throws Exception {
        log.info("bookingDto = {}", bookingResponseShortDto1);
        JsonContent<BookingResponseShortDto> result = json.write(bookingResponseShortDto1);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(bookingResponseShortDto1.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.bookerId")
                .isEqualTo(bookingResponseShortDto1.getId().intValue());

    }
}