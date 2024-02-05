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
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
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
class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;

    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private Item item2;
    private Item item3;
    private BookingResponseDto booking1;
    private BookingResponseDto booking2;
    private BookingResponseDto booking3;
    private BookingRequestDto bookingRequest1;


    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .name("user1")
                .email("user@ya.ru")
                .build();
        user2 = User.builder()
                .name("user2")
                .email("user@google.ru")
                .build();
        user3 = User.builder()
                .name("user3")
                .email("user@mail.ru")
                .build();

        item1 = Item.builder()
                .name("item1")
                .description("item1")
                .available(true)
                .owner(user1)
                .build();
        item2 = Item.builder()
                .name("item2")
                .description("item2")
                .available(false)
                .owner(user2)
                .build();
        item3 = Item.builder()
                .name("item3")
                .description("item3")
                .available(true)
                .owner(user3)
                .build();

        bookingRequest1 = BookingRequestDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .build();

        booking1 = BookingResponseDto.builder()
                .id(1L)
                .start(LocalDateTime.now().plusMonths(1))
                .end(LocalDateTime.now().plusMonths(2))
                .item(item1)
                .booker(user1)
                .status(Status.APPROVED)
                .build();
        booking2 = BookingResponseDto.builder()
                .id(2L)
                .start(LocalDateTime.now().plusMonths(2))
                .end(LocalDateTime.now().plusMonths(3))
                .item(item2)
                .booker(user2)
                .status(Status.APPROVED)
                .build();
        booking3 = BookingResponseDto.builder()
                .id(3L)
                .start(LocalDateTime.now().plusMonths(3))
                .end(LocalDateTime.now().plusMonths(4))
                .item(item3)
                .booker(user3)
                .status(Status.APPROVED)
                .build();
    }

    @Test
    void shouldAddNewBooking() throws Exception {
        Mockito.when(bookingService.addNewBooking(Mockito.anyLong(), Mockito.any())).thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(booking1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .addNewBooking(1L, bookingRequest1);
    }

    @Test
    void shouldApproveBooking() throws Exception {
        Mockito.when(bookingService.approveBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(booking1);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .param("approved", "true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .approveBooking(1L, 1L, Status.APPROVED);
    }

    @Test
    void shouldGetBookingById() throws Exception {
        Mockito.when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(booking1);

        mvc.perform(get("/bookings/{bookingId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$.booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(booking1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .getBookingById(1L, 1L);
    }

    @Test
    void shouldGetAllBookings() throws Exception {
        Mockito.when(bookingService.getAllBookings(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(Collections.singleton(booking1));

        mvc.perform(get("/bookings")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$[0].booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(booking1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllBookings(1L, "ALL", 0, 10);
    }

    @Test
    void shouldGetAllOwnerBookings() throws Exception {
        Mockito.when(bookingService.getAllOwnerBookings(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(Collections.singleton(booking1));

        mvc.perform(get("/bookings/owner")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(booking1.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(booking1.getStatus().toString()), Status.class))
                .andExpect(jsonPath("$[0].booker.id", is(booking1.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(booking1.getItem().getId()), Long.class));
        Mockito.verify(bookingService, Mockito.times(1))
                .getAllOwnerBookings(1L, "ALL", 0, 10);
    }
}