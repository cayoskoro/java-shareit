package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.ItemBaseTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@TestPropertySource(properties = "db.name=test")
class ItemControllerTest extends ItemBaseTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetAllUserItems() throws Exception {
        Mockito.when(itemService.getAllUserItems(Mockito.anyLong(), Mockito.anyInt(),
                Mockito.anyInt())).thenReturn(Collections.singletonList(itemResponseDto2));

        mvc.perform(get("/items")
                        .param("state", "ALL")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemResponseDto2.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemResponseDto2.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available",
                        is(itemResponseDto2.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].lastBooking.id",
                        is(itemResponseDto2.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].lastBooking.bookerId",
                        is(itemResponseDto2.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.id",
                        is(itemResponseDto2.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$[0].nextBooking.bookerId",
                        is(itemResponseDto2.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$[0].comments[0].id",
                        is(commentDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].comments[0].text",
                        is(commentDto1.getText()), String.class))
                .andExpect(jsonPath("$[0].comments[0].authorName",
                        is(commentDto1.getAuthorName()), String.class))
                .andExpect(jsonPath("$[0].requestId", is(itemResponseDto2.getRequestId()), Long.class));
        Mockito.verify(itemService, Mockito.times(1))
                .getAllUserItems(1L, 0, 10);
    }

    @Test
    void shouldGetItemById() throws Exception {
        Mockito.when(itemService.getItemById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemResponseDto1);

        mvc.perform(get("/items/{itemId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponseDto1.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemResponseDto1.getDescription()), String.class))
                .andExpect(jsonPath("$.available",
                        is(itemResponseDto1.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.lastBooking.id",
                        is(itemResponseDto1.getLastBooking().getId()), Long.class))
                .andExpect(jsonPath("$.lastBooking.bookerId",
                        is(itemResponseDto1.getLastBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.id",
                        is(itemResponseDto1.getNextBooking().getId()), Long.class))
                .andExpect(jsonPath("$.nextBooking.bookerId",
                        is(itemResponseDto1.getNextBooking().getBookerId()), Long.class))
                .andExpect(jsonPath("$.comments[0].id",
                        is(commentDto1.getId()), Long.class))
                .andExpect(jsonPath("$.comments[0].text",
                        is(commentDto1.getText()), String.class))
                .andExpect(jsonPath("$.comments[0].authorName",
                        is(commentDto1.getAuthorName()), String.class))
                .andExpect(jsonPath("$.requestId", is(itemResponseDto1.getRequestId()), Long.class));
        Mockito.verify(itemService, Mockito.times(1))
                .getItemById(1L, 1L);
    }

    @Test
    void shouldAddNewItem() throws Exception {
        Mockito.when(itemService.addNewItem(Mockito.anyLong(), Mockito.any())).thenReturn(itemResponseDto1);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponseDto1.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemResponseDto1.getDescription()), String.class))
                .andExpect(jsonPath("$.available",
                        is(itemResponseDto1.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(itemResponseDto1.getRequestId()), Long.class));
        Mockito.verify(itemService, Mockito.times(1))
                .addNewItem(1L, itemRequestDto1);
    }

    @Test
    void shouldEditItem() throws Exception {
        Mockito.when(itemService.editItem(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(itemResponseDto1);

        mvc.perform(patch("/items/{itemId}", 1L)
                        .content(mapper.writeValueAsString(itemRequestDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemResponseDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemResponseDto1.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemResponseDto1.getDescription()), String.class))
                .andExpect(jsonPath("$.available",
                        is(itemResponseDto1.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(itemResponseDto1.getRequestId()), Long.class));
        Mockito.verify(itemService, Mockito.times(1))
                .editItem(1L, 1L, itemRequestDto1);
    }

    @Test
    void shouldSearchItems() throws Exception {
        Mockito.when(itemService.searchItems(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(Collections.singleton(itemResponseDto2));

        mvc.perform(get("/items/search")
                        .param("text", "item")
                        .param("from", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemResponseDto2.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemResponseDto2.getName()), String.class))
                .andExpect(jsonPath("$[0].description", is(itemResponseDto2.getDescription()), String.class))
                .andExpect(jsonPath("$[0].available",
                        is(itemResponseDto2.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$[0].requestId", is(itemResponseDto2.getRequestId()), Long.class));
        Mockito.verify(itemService, Mockito.times(1))
                .searchItems("item", 0, 10);
    }

    @Test
    void shouldAddNewComment() throws Exception {
        Mockito.when(itemService.addNewComment(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
                .thenReturn(commentDto1);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .content(mapper.writeValueAsString(commentDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER_USER_ID, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto1.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto1.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentDto1.getAuthorName()), String.class));
        Mockito.verify(itemService, Mockito.times(1))
                .addNewComment(1L, 1L, commentDto1);
    }
}