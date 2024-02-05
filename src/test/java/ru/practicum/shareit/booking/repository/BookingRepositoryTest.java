package ru.practicum.shareit.booking.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class BookingRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private BookingRepository bookingRepository;
    private final PageRequest pageRequest = PageRequest.of(0, 10);
    private User user1;
    private User user2;
    private User user3;
    private Item item1;
    private Item item2;
    private Item item3;
    private Booking booking1;
    private Booking booking2;
    private Booking booking3;


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
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

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
        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
        item3 = itemRepository.save(item3);

        booking1 = new Booking();
        booking1.setStart(LocalDateTime.now());
        booking1.setEnd(LocalDateTime.now());
        booking1.setItem(item1);
        booking1.setBooker(user1);
        booking1.setStatus(Status.WAITING);

        booking2 = new Booking();
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(LocalDateTime.now());
        booking2.setItem(item2);
        booking2.setBooker(user2);
        booking2.setStatus(Status.WAITING);

        booking3 = new Booking();
        booking3.setStart(LocalDateTime.now());
        booking3.setEnd(LocalDateTime.now());
        booking3.setItem(item3);
        booking3.setBooker(user3);
        booking3.setStatus(Status.WAITING);
    }

    @Test
    void shouldFindAllByBookerIdOrderByStartDesc() {
        booking2.setBooker(user1);
        booking1.setStart(LocalDateTime.now().minusMonths(2));
        booking2.setStart(LocalDateTime.now());

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
        booking1.setStart(LocalDateTime.now().minusMonths(2));
        booking1.setEnd(LocalDateTime.now().plusMonths(2));
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(LocalDateTime.now());
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
        booking1.setStart(LocalDateTime.now().minusMonths(2));
        booking1.setEnd(LocalDateTime.now().plusMonths(1));
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(LocalDateTime.now().minusMonths(5));
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
        booking1.setStart(LocalDateTime.now().plusMonths(3));
        booking1.setEnd(LocalDateTime.now().plusMonths(1));
        booking2.setStart(LocalDateTime.now());
        booking2.setEnd(LocalDateTime.now().minusMonths(5));
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
        booking1.setStatus(Status.APPROVED);
        booking2.setBooker(user1);
        booking3.setBooker(user1);
        booking1.setStart(LocalDateTime.now().minusMonths(3));
        booking2.setStart(LocalDateTime.now());
        booking3.setStart(LocalDateTime.now().plusMonths(3));
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
        booking1.setStart(LocalDateTime.now().minusMonths(3));
        booking2.setStart(LocalDateTime.now().minusMonths(2));
        booking3.setStart(LocalDateTime.now().plusMonths(1));
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
        booking1.setStart(LocalDateTime.now().minusMonths(3));
        booking2.setStart(LocalDateTime.now().minusMonths(2));
        booking3.setStart(LocalDateTime.now().minusMonths(3));
        booking1.setEnd(LocalDateTime.now().minusMonths(3));
        booking2.setEnd(LocalDateTime.now().minusMonths(2));
        booking3.setEnd(LocalDateTime.now().plusMonths(3));
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
        booking1.setStart(LocalDateTime.now().minusMonths(3));
        booking2.setStart(LocalDateTime.now().minusMonths(2));
        booking3.setStart(LocalDateTime.now().minusMonths(3));
        booking1.setEnd(LocalDateTime.now().minusMonths(3));
        booking2.setEnd(LocalDateTime.now().minusMonths(2));
        booking3.setEnd(LocalDateTime.now().plusMonths(3));
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
        booking1.setStart(LocalDateTime.now().plusMonths(1));
        booking2.setStart(LocalDateTime.now().plusMonths(2));
        booking3.setStart(LocalDateTime.now().plusMonths(3));
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
        booking1.setStatus(Status.APPROVED);
        item3.setOwner(user1);
        item3 = itemRepository.save(item3);
        booking2.setItem(item3);
        booking1.setStart(LocalDateTime.now().plusMonths(3));
        booking2.setStart(LocalDateTime.now().plusMonths(2));
        booking3.setStart(LocalDateTime.now().plusMonths(3));
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
        booking1.setEnd(LocalDateTime.now().minusMonths(1));
        booking1 = bookingRepository.save(booking1);
        booking2 = bookingRepository.save(booking2);
        booking3 = bookingRepository.save(booking3);

        assertTrue(bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndIsBeforeOrderByEndDesc(user1.getId(),
                item1.getId(), Status.APPROVED, LocalDateTime.now()));
    }
}