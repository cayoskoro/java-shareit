package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

public class ItemBaseTest {
    protected static final String HEADER_USER_ID = "X-Sharer-User-Id";
    protected static final LocalDateTime LOCAL_DATE_TIME_NOW_WITH_NANO = LocalDateTime.now().withNano(0);
    protected final PageRequest pageRequest = PageRequest.of(0, 10);
    protected ItemResponseDto itemResponseDto1;
    protected ItemResponseDto itemResponseDto2;
    protected ItemRequestDto itemRequestDto1;
    protected ItemRequest itemRequest1;
    protected ItemRequest itemRequest2;
    protected ItemRequest itemRequest3;
    protected User user1;
    protected User user2;
    protected User user3;
    protected Item item1;
    protected Item item2;
    protected Item item3;
    protected Comment comment1;
    protected Comment comment2;
    protected Comment comment3;
    protected CommentDto commentDto1;
    protected CommentDto commentDto2;
    protected Booking booking1;
    protected Booking booking2;
    protected BookingResponseShortDto bookingResponseShortDto1;
    protected BookingResponseShortDto bookingResponseShortDto2;
    protected BookingResponseDto bookingResponseDto1;

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
                .request(itemRequest2)
                .build();
        item2 = Item.builder()
                .id(2L)
                .name("item2")
                .description("item2")
                .available(false)
                .owner(user2)
                .request(itemRequest3)
                .build();
        item3 = Item.builder()
                .id(3L)
                .name("item3")
                .description("item3")
                .available(true)
                .owner(user3)
                .request(itemRequest1)
                .build();

        comment1 = new Comment();
        comment1.setId(1L);
        comment1.setText("comment1");
        comment1.setItem(item2);
        comment1.setAuthor(user1);
        comment1.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO);
        comment2 = new Comment();
        comment1.setId(2L);
        comment2.setText("comment2");
        comment2.setItem(item1);
        comment2.setAuthor(user1);
        comment2.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO);
        comment3 = new Comment();
        comment1.setId(3L);
        comment3.setText("comment3");
        comment3.setItem(item1);
        comment3.setAuthor(user2);
        comment3.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO);

        commentDto1 = CommentDto.builder()
                .id(1L)
                .text("comment1")
                .authorName("user1")
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .build();

        commentDto2 = CommentDto.builder()
                .id(2L)
                .text("comment2")
                .authorName("user2")
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .build();

        booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(1));
        booking1.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking1.setItem(item1);
        booking1.setBooker(user1);
        booking1.setStatus(Status.APPROVED);

        booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(1));
        booking2.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking2.setItem(item1);
        booking2.setBooker(user1);
        booking2.setStatus(Status.APPROVED);

        bookingResponseShortDto1 = BookingResponseShortDto.builder()
                .id(1L)
                .bookerId(1L)
                .build();

        bookingResponseShortDto2 = BookingResponseShortDto.builder()
                .id(2L)
                .bookerId(2L)
                .build();

        bookingResponseDto1 = BookingResponseDto.builder()
                .id(1L)
                .start(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .end(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .item(item1)
                .booker(user1)
                .status(Status.APPROVED)
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
                .comments(List.of(commentDto1))
                .requestId(2L)
                .build();

        itemRequestDto1 = ItemRequestDto.builder()
                .id(1L)
                .name("item1")
                .description("item1")
                .available(true)
                .requestId(1L)
                .build();
    }

}
