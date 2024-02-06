package ru.practicum.shareit.booking.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.BookingBaseTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class BookingRepositoryTest extends BookingBaseTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        itemRequest1 = itemRequestRepository.save(itemRequest1);

        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
        item3 = itemRepository.save(item3);

    }

    @Test
    void shouldFindAllByBookerIdOrderByStartDesc() {
        booking2.setBooker(user1);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO);

        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(user1.getId(), pageRequest)
                .getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertIterableEquals(List.of(booking2, booking1), bookings);
    }

    @Test
    void shouldFindAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        booking2.setBooker(user1);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
        booking1.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(2));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking2.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        user1.getId(), LocalDateTime.now(), LocalDateTime.now(), pageRequest)
                .getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertIterableEquals(List.of(booking1), bookings);
    }

    @Test
    void shouldFindAllByBookerIdAndEndBeforeOrderByStartDesc() {
        booking2.setBooker(user1);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
        booking1.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(1));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking2.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(5));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(
                        user1.getId(), LocalDateTime.now(), pageRequest)
                .getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertIterableEquals(List.of(booking2), bookings);
    }

    @Test
    void shouldFindAllByBookerIdAndStartAfterOrderByStartDesc() {
        booking2.setBooker(user1);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(3));
        booking1.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(1));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking2.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(5));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(
                        user1.getId(), LocalDateTime.now(), pageRequest)
                .getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertIterableEquals(List.of(booking1), bookings);
    }

    @Test
    void shouldFindAllByBookerIdAndStatusOrderByStartDesc() {
        booking2.setStatus(Status.WAITING);
        booking3.setStatus(Status.WAITING);
        booking2.setBooker(user1);
        booking3.setBooker(user1);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO);
        booking3.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(3));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(user1.getId(),
                Status.WAITING, pageRequest).getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertIterableEquals(List.of(booking3, booking2), bookings);
    }

    @Test
    void shouldFindAllByItemOwnerId() {
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerId(user2.getId());
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertIterableEquals(List.of(booking2), bookings);
    }

    @Test
    void shouldFindAllByItemOwnerIdAndItemIn() {
        item3.setOwner(user2);
        item3 = itemRepository.save(item3);
        booking3.setItem(item3);
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndItemIn(user2.getId(), List.of(item2));
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertIterableEquals(List.of(booking2), bookings);
    }

    @Test
    void shouldFindAllByItemOwnerIdOrderByStartDesc() {
        item3.setOwner(user1);
        item3 = itemRepository.save(item3);
        booking3.setItem(item3);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
        booking3.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(1));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(user1.getId(),
                pageRequest).getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertIterableEquals(List.of(booking3, booking1), bookings);
    }

    @Test
    void shouldFindAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        item3.setOwner(user1);
        item3 = itemRepository.save(item3);
        booking3.setItem(item3);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
        booking3.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        booking1.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        booking2.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
        booking3.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(3));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                user1.getId(), LocalDateTime.now(), LocalDateTime.now(), pageRequest).getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertIterableEquals(List.of(booking3), bookings);
    }

    @Test
    void shouldFindAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
        item3.setOwner(user1);
        item3 = itemRepository.save(item3);
        booking3.setItem(item3);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
        booking3.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        booking1.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        booking2.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
        booking3.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(3));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
                user1.getId(), LocalDateTime.now(), pageRequest).getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertIterableEquals(List.of(booking1), bookings);
    }

    @Test
    void shouldFindAllByItemOwnerIdAndStartAfterOrderByStartDesc() {
        item3.setOwner(user1);
        item3 = itemRepository.save(item3);
        booking3.setItem(item3);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(1));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(2));
        booking3.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(3));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                user1.getId(), LocalDateTime.now(), pageRequest).getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(2, bookings.size());
        assertIterableEquals(List.of(booking3, booking1), bookings);
    }

    @Test
    void shouldFindAllByItemOwnerIdAndStatusOrderByStartDesc() {
        booking2.setStatus(Status.WAITING);
        booking3.setStatus(Status.WAITING);
        item3.setOwner(user1);
        item3 = itemRepository.save(item3);
        booking2.setItem(item3);
        booking1.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(3));
        booking2.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(2));
        booking3.setStart(LOCAL_DATE_TIME_NOW_WITH_NANO.plusMonths(3));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(user1.getId(),
                Status.APPROVED, pageRequest).getContent();
        log.info("bookings = {}", bookings);
        assertNotNull(bookings);
        assertEquals(1, bookings.size());
        assertIterableEquals(List.of(booking1), bookings);
    }

    @Test
    void existsByBookerIdAndItemIdAndStatusAndEndIsBeforeOrderByEndDesc() {
        booking1.setStatus(Status.APPROVED);
        booking1.setEnd(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(1));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        assertTrue(bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndIsBeforeOrderByEndDesc(user1.getId(),
                item1.getId(), Status.APPROVED, LocalDateTime.now()));
    }
}