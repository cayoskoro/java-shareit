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
class BookingResponseDtoTest extends BookingBaseTest {
    private final JacksonTester<BookingResponseDto> json;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetBookingResponseDto() throws Exception {
        log.info("bookingDto = {}", bookingResponseDto1);
        JsonContent<BookingResponseDto> result = json.write(bookingResponseDto1);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(bookingResponseDto1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(bookingResponseDto1.getStart().toString());
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(bookingResponseDto1.getEnd().toString());
        assertThat(result).extractingJsonPathNumberValue("$.item.id").isEqualTo(item1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.item.name").isEqualTo(item1.getName());
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo(item1.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.item.available").isEqualTo(item1.isAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.booker.id").isEqualTo(user1.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.booker.name").isEqualTo(user1.getName());
        assertThat(result).extractingJsonPathStringValue("$.booker.email").isEqualTo(user1.getEmail());
        assertThat(result).extractingJsonPathStringValue("$.status").isEqualTo("APPROVED");

    }
}