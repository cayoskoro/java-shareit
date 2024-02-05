package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@TestPropertySource(properties = "db.name=test")
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;

    private ItemResponseDto itemResponseDto1;
    private ItemResponseDto itemResponseDto2;
    private CommentDto commentDto1;
    private CommentDto commentDto2;
    private ItemRequestDto itemRequestDto1;

    @BeforeEach
    void setUp() {
        BookingResponseShortDto bookingResponseShortDto1 = BookingResponseShortDto.builder()
                .id(1L)
                .bookerId(1L)
                .build();

        BookingResponseShortDto bookingResponseShortDto2 = BookingResponseShortDto.builder()
                .id(2L)
                .bookerId(2L)
                .build();

        commentDto1 = CommentDto.builder()
                .id(1L)
                .text("item1")
                .authorName("user1")
                .created(LocalDateTime.now())
                .build();

        commentDto2 = CommentDto.builder()
                .id(1L)
                .text("item2")
                .authorName("user2")
                .created(LocalDateTime.now())
                .build();

        itemResponseDto1 = ItemResponseDto.builder()
                .id(1L)
                .name("item1")
                .description("item1")
                .available(true)
                .lastBooking(bookingResponseShortDto2)
                .nextBooking(bookingResponseShortDto1)
                .comments(List.of(commentDto1))
                .requestId(1L)
                .build();

        itemResponseDto2 = ItemResponseDto.builder()
                .id(2L)
                .name("item2")
                .description("item2")
                .available(true)
                .lastBooking(bookingResponseShortDto1)
                .nextBooking(bookingResponseShortDto2)
                .comments(List.of(commentDto2))
                .requestId(2L)
                .build();

        itemRequestDto1 = ItemRequestDto.builder()
                .id(1L)
                .description("itemRequest1")
                .created(LocalDateTime.now())
                .items(List.of(itemResponseDto2))
                .build();
    }

    @Test
    void shouldAddNewItemRequest() throws Exception {
        Mockito.when(itemRequestService.addNewItemRequest(Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemRequestDto1);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription()), String.class));
        Mockito.verify(itemRequestService, Mockito.times(1))
                .addNewItemRequest(1L, itemRequestDto1);
    }

    @Test
    void shouldGetOwnerItemRequests() throws Exception {
        Mockito.when(itemRequestService.getOwnerItemRequests(Mockito.anyLong()))
                .thenReturn(Collections.singletonList(itemRequestDto1));

        mvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0].id", is(itemResponseDto2.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is(itemResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$[0].items[0].description", is(itemResponseDto2.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0].available",
                        is(itemResponseDto2.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].items[0].lastBooking.id",
                        is(itemResponseDto2.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].lastBooking.bookerId",
                        is(itemResponseDto2.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].nextBooking.id",
                        is(itemResponseDto2.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].nextBooking.bookerId",
                        is(itemResponseDto2.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].id",
                        is(commentDto2.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].text",
                        is(commentDto2.getText()), String.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].authorName",
                        is(commentDto2.getAuthorName()), String.class))
                .andExpect(jsonPath("$[0].items[0].requestId",
                        is(itemResponseDto2.getRequestId()), Long.class));
        Mockito.verify(itemRequestService, Mockito.times(1))
                .getOwnerItemRequests(1L);
    }

    @Test
    void shouldGetAllItemRequests() throws Exception {
        Mockito.when(itemRequestService.getAllItemRequests(Mockito.anyLong(), Mockito.anyInt(),
                        Mockito.anyInt()))
                .thenReturn(Collections.singletonList(itemRequestDto1));

        mvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0].id", is(itemResponseDto2.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is(itemResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$[0].items[0].description", is(itemResponseDto2.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0].available",
                        is(itemResponseDto2.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].items[0].lastBooking.id",
                        is(itemResponseDto2.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].lastBooking.bookerId",
                        is(itemResponseDto2.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].nextBooking.id",
                        is(itemResponseDto2.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].nextBooking.bookerId",
                        is(itemResponseDto2.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].id",
                        is(commentDto2.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].text",
                        is(commentDto2.getText()), String.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].authorName",
                        is(commentDto2.getAuthorName()), String.class))
                .andExpect(jsonPath("$[0].items[0].requestId",
                        is(itemResponseDto2.getRequestId()), Long.class));
        Mockito.verify(itemRequestService, Mockito.times(1))
                .getAllItemRequests(1L, 0, 10);
    }

    @Test
    void shouldGetItemRequest() throws Exception {
        Mockito.when(itemRequestService.getItemRequest(Mockito.anyLong(), Mockito.anyLong()))
                .thenReturn(itemRequestDto1);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription()), String.class))
                .andExpect(jsonPath("$.items[0].id", is(itemResponseDto2.getId()), Long.class))
                .andExpect(jsonPath("$.items[0].name", is(itemResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$.items[0].description", is(itemResponseDto2.getDescription()), String.class))
                .andExpect(jsonPath("$.items[0].available",
                        is(itemResponseDto2.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.items[0].lastBooking.id",
                        is(itemResponseDto2.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.items[0].lastBooking.bookerId",
                        is(itemResponseDto2.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.items[0].nextBooking.id",
                        is(itemResponseDto2.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.items[0].nextBooking.bookerId",
                        is(itemResponseDto2.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.items[0].comments[0].id",
                        is(commentDto2.getId()), Long.class))
                .andExpect(jsonPath("$.items[0].comments[0].text",
                        is(commentDto2.getText()), String.class))
                .andExpect(jsonPath("$.items[0].comments[0].authorName",
                        is(commentDto2.getAuthorName()), String.class))
                .andExpect(jsonPath("$.items[0].requestId",
                        is(itemResponseDto2.getRequestId()), Long.class));
        Mockito.verify(itemRequestService, Mockito.times(1))
                .getItemRequest(1L, 1L);
    }
}