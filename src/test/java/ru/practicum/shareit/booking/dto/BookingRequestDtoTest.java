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

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class BookingRequestDtoTest extends BookingBaseTest {
    private final JacksonTester<BookingRequestDto> json;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetBookingRequestDto() throws Exception {
        log.info("bookingDto = {}", bookingRequestDto1);
        JsonContent<BookingRequestDto> result = json.write(bookingRequestDto1);
        assertThat(result).extractingJsonPathNumberValue("$.itemId")
                .isEqualTo(bookingRequestDto1.getItemId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingRequestDto1.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingRequestDto1.getEnd().toString());
    }
}