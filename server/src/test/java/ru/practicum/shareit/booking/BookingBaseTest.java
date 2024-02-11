package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingResponseShortDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

public class BookingBaseTest {
    protected static final String HEADER_USER_ID = "X-Sharer-User-Id";
    protected static final LocalDateTime LOCAL_DATE_TIME_NOW_WITH_NANO = LocalDateTime.now().withNano(0);
    protected final PageRequest pageRequest = PageRequest.of(0, 10);
    protected User user1;
    protected User user2;
    protected User user3;
    protected Item item1;
    protected Item item2;
    protected Item item3;
    protected ItemRequest itemRequest1;
    protected Booking booking1;
    protected Booking booking2;
    protected Booking booking3;
    protected BookingRequestDto bookingRequestDto1;
    protected BookingResponseDto bookingResponseDto1;
    protected BookingResponseShortDto bookingResponseShortDto1;

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

        item1 = Item.builder()
                .id(1L)
                .name("item1")
                .description("item1")
                .available(true)
                .owner(user1)
                .request(itemRequest1)
                .build();
        item2 = Item.builder()
                .id(2L)
                .name("item2")
                .description("item2")
                .available(false)
                .owner(user2)
                .build();
        item3 = Item.builder()
                .id(3L)
                .name("item3")
                .description("item3")
                .available(true)
                .owner(user3)
                .build();

        itemRequest1 = ItemRequest.builder()
                .description("request1")
                .requestor(user1)
                .created(LOCAL_DATE_TIME_NOW_WITH_NANO)
                .build();

        booking1 = new Booking();
        booking1.setId(1L);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking1.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking1.setItem(item1);
        booking1.setBooker(user1);
        booking1.setStatus(Status.APPROVED);

        booking2 = new Booking();
        booking2.setId(2L);
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking2.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking2.setItem(item2);
        booking2.setBooker(user2);
        booking2.setStatus(Status.APPROVED);

        booking3 = new Booking();
        booking3.setId(3L);
        booking3.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking3.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking3.setItem(item3);
        booking3.setBooker(user3);
        booking3.setStatus(Status.APPROVED);

        bookingRequestDto1 = BookingRequestDto.builder()
                .itemId(item1.getId())
                .start(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(1))
                .end(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(2))
                .build();

        bookingResponseDto1 = BookingResponseDto.builder()
                .id(1L)
                .start(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(1))
                .end(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(2))
                .item(item1)
                .booker(user1)
                .status(Status.APPROVED)
                .build();

        bookingResponseShortDto1 = BookingResponseShortDto.builder()
                .id(1L)
                .bookerId(1L)
                .build();
    }
}
