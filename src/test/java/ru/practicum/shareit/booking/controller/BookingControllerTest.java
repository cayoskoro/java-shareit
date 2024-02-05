package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;


@WebMvcTest(controllers = BookingController.class)
@TestPropertySource(properties = "db.name=test")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingControllerTest {
    private final ObjectMapper mapper;
    @MockBean
    private final BookingService bookingService;
    private MockMvc mvc;


    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldAddNewBooking() {
    }

    @Test
    void shouldApproveBooking() {
    }

    @Test
    void shouldGetBookingById() {
    }

    @Test
    void shouldGetAllBookings() {
    }

    @Test
    void shouldGetAllOwnerBookings() {
    }
}