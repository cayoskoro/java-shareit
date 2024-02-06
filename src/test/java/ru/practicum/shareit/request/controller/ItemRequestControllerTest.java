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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.request.ItemRequestBaseTest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@TestPropertySource(properties = "db.name=test")
class ItemRequestControllerTest extends ItemRequestBaseTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldAddNewItemRequest() throws Exception {
        Mockito.when(itemRequestService.addNewItemRequest(Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemRequestDto1);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
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
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0].id", is(itemResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is(itemResponseDto1.getName()), String.class))
                .andExpect(jsonPath("$[0].items[0].description", is(itemResponseDto1.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0].available",
                        is(itemResponseDto1.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].items[0].lastBooking.id",
                        is(itemResponseDto1.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].lastBooking.bookerId",
                        is(itemResponseDto1.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].nextBooking.id",
                        is(itemResponseDto1.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].nextBooking.bookerId",
                        is(itemResponseDto1.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].id",
                        is(commentDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].text",
                        is(commentDto1.getText()), String.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].authorName",
                        is(commentDto1.getAuthorName()), String.class))
                .andExpect(jsonPath("$[0].items[0].requestId",
                        is(itemResponseDto1.getRequestId()), Long.class));
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
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDto1.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0].id", is(itemResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].name", is(itemResponseDto1.getName()), String.class))
                .andExpect(jsonPath("$[0].items[0].description", is(itemResponseDto1.getDescription()), String.class))
                .andExpect(jsonPath("$[0].items[0].available",
                        is(itemResponseDto1.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].items[0].lastBooking.id",
                        is(itemResponseDto1.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].lastBooking.bookerId",
                        is(itemResponseDto1.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].nextBooking.id",
                        is(itemResponseDto1.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].nextBooking.bookerId",
                        is(itemResponseDto1.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].id",
                        is(commentDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].text",
                        is(commentDto1.getText()), String.class))
                .andExpect(jsonPath("$[0].items[0].comments[0].authorName",
                        is(commentDto1.getAuthorName()), String.class))
                .andExpect(jsonPath("$[0].items[0].requestId",
                        is(itemResponseDto1.getRequestId()), Long.class));
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
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDto1.getDescription()), String.class))
                .andExpect(jsonPath("$.items[0].id", is(itemResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$.items[0].name", is(itemResponseDto1.getName()), String.class))
                .andExpect(jsonPath("$.items[0].description", is(itemResponseDto1.getDescription()), String.class))
                .andExpect(jsonPath("$.items[0].available",
                        is(itemResponseDto1.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.items[0].lastBooking.id",
                        is(itemResponseDto1.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.items[0].lastBooking.bookerId",
                        is(itemResponseDto1.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.items[0].nextBooking.id",
                        is(itemResponseDto1.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.items[0].nextBooking.bookerId",
                        is(itemResponseDto1.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.items[0].comments[0].id",
                        is(commentDto1.getId()), Long.class))
                .andExpect(jsonPath("$.items[0].comments[0].text",
                        is(commentDto1.getText()), String.class))
                .andExpect(jsonPath("$.items[0].comments[0].authorName",
                        is(commentDto1.getAuthorName()), String.class))
                .andExpect(jsonPath("$.items[0].requestId",
                        is(itemResponseDto1.getRequestId()), Long.class));
        Mockito.verify(itemRequestService, Mockito.times(1))
                .getItemRequest(1L, 1L);
    }
}