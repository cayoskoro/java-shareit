package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingBaseTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.UnsupportedStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = BookingController.class)
@TestPropertySource(properties = "db.name=test")
class BookingControllerTest extends BookingBaseTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldAddNewBooking() throws Exception {
        Mockito.when(bookingService.addNewBooking(Mockito.anyLong(), Mockito.any())).thenReturn(bookingResponseDto1);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingResponseDto1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(bookingResponseDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .addNewBooking(1L, bookingRequestDto1);
    }

    @Test
    void shouldAddNewBookingWithNotFound() throws Exception {
        Mockito.when(bookingService.addNewBooking(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new NotFoundException(
                        "Бронирование недоступно. Пользователь по id = 1 является владельцем вещи по id = 1"));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isNotFound());
        Mockito.verify(bookingService, Mockito.times(1))
                .addNewBooking(1L, bookingRequestDto1);
    }

    @Test
    void shouldAddNewBookingWithNotAvailable() throws Exception {
        Mockito.when(bookingService.addNewBooking(Mockito.anyLong(), Mockito.any()))
                .thenThrow(new NotAvailableException("Вещь по id = 1 в данный момент недоступна"));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingRequestDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isBadRequest());
        Mockito.verify(bookingService, Mockito.times(1))
                .addNewBooking(1L, bookingRequestDto1);
    }

    @Test
    void shouldApproveBooking() throws Exception {
        Mockito.when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(bookingResponseDto1);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingResponseDto1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(bookingResponseDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .approveBooking(1L, 1L, Status.APPROVED);
    }

    @Test
    void shouldApproveBookingWithIncorrectParameter() throws Exception {
        Mockito.when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenThrow(new IncorrectParameterException("Аренда по id = 1 уже одобрена владельцем по id = 1"));

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isBadRequest());
        Mockito.verify(bookingService, Mockito.times(1))
                .approveBooking(1L, 1L, Status.APPROVED);
    }

    @Test
    void shouldApproveBookingWithNotFound() throws Exception {
        Mockito.when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenThrow(new NotFoundException("Пользователь по id = 1 не является владельцом аренды - 1"));

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isNotFound());
        Mockito.verify(bookingService, Mockito.times(1))
                .approveBooking(1L, 1L, Status.APPROVED);
    }

    @Test
    void shouldGetBookingById() throws Exception {
        Mockito.when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(bookingResponseDto1);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(bookingResponseDto1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(bookingResponseDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingResponseDto1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .getBookingById(1L, 1L);
    }

    @Test
    void shouldGetBookingByIdWithInternalError() throws Exception {
        Mockito.when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenThrow(new RuntimeException("InternalError"));

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isInternalServerError());
        Mockito.verify(bookingService, Mockito.times(1))
                .getBookingById(1L, 1L);
    }

    @Test
    void shouldGetAllBookings() throws Exception {
        Mockito.when(bookingService.getAllBookings(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(Collections.singleton(bookingResponseDto1));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingResponseDto1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingResponseDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingResponseDto1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllBookings(1L, "ALL", 0, 10);
    }

    @Test
    void shouldGetAllBookingsWithUnsupported() throws Exception {
        Mockito.when(bookingService.getAllBookings(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(),
                        Mockito.anyInt()))
                .thenThrow(new UnsupportedStateException("Unknown state: UNSUPPORTED"));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isBadRequest());
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllBookings(1L, "ALL", 0, 10);
    }

    @Test
    void shouldGetAllOwnerBookings() throws Exception {
        Mockito.when(bookingService.getAllOwnerBookings(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(Collections.singleton(bookingResponseDto1));

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(bookingResponseDto1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$[0].booker.id", is(bookingResponseDto1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingResponseDto1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllOwnerBookings(1L, "ALL", 0, 10);
    }
}