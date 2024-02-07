package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemRequestBaseTest {
    protected final PageRequest pageRequest = PageRequest.of(0, 10);
    protected static final LocalDateTime LOCAL_DATE_TIME_NOW_WITH_NANO = LocalDateTime.now().withNano(0);
    protected static final String HEADER_USER_ID = "X-Sharer-User-Id";
    protected ItemRequest itemRequest1;
    protected ItemRequest itemRequest2;
    protected ItemRequest itemRequest3;
    protected User user1;
    protected User user2;
    protected User user3;
    protected Item item1;
    protected ItemRequestDto itemRequestDto1;
    protected ItemResponseDto itemResponseDto1;
    protected ItemResponseDto itemResponseDto2;
    protected BookingResponseShortDto bookingResponseShortDto1;
    protected BookingResponseShortDto bookingResponseShortDto2;
    protected CommentDto commentDto1;
    protected CommentDto commentDto2;

    @BeforeEach
    protected void setUp() {
        user1 = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@ya.ru")
                .build();
        user2 = User.builder()
                .id(2L)
                .name("user2")
                .email("user2@google.ru")
                .build();
        user3 = User.builder()
                .id(3L)
                .name("user3")
                .email("user3@mail.ru")
                .build();

        itemRequest1 = ItemRequest.builder()
                .id(1L)
                .description("request1")
                .requestor(user1)
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .build();

        itemRequest2 = ItemRequest.builder()
                .id(2L)
                .description("request2")
                .requestor(user2)
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .build();

        itemRequest3 = ItemRequest.builder()
                .id(3L)
                .description("request3")
                .requestor(user3)
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .build();

        item1 = Item.builder()
                .id(1L)
                .name("item1")
                .description("item1")
                .available(true)
                .owner(user1)
                .request(itemRequest1)
                .build();

        bookingResponseShortDto1 = BookingResponseShortDto.builder()
                .id(1L)
                .bookerId(1L)
                .build();

        bookingResponseShortDto2 = BookingResponseShortDto.builder()
                .id(2L)
                .bookerId(2L)
                .build();

        commentDto1 = CommentDto.builder()
                .id(1L)
                .text("item1")
                .authorName("user1")
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .build();

        commentDto2 = CommentDto.builder()
                .id(2L)
                .text("item2")
                .authorName("user2")
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .build();

        itemResponseDto1 = ItemResponseDto.builder()
                .id(1L)
                .name("item1")
                .description("item1")
                .available(true)
                .lastBooking(bookingResponseShortDto1)
                .nextBooking(bookingResponseShortDto2)
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
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .items(List.of(itemResponseDto1))
                .build();
    }
}
