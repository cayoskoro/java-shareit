package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
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
class ItemRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private final PageRequest pageRequest = PageRequest.of(0, 10);
    private User user1;
    private User user2;
    private User user3;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private ItemRequest itemRequest3;
    private Item item1;
    private Item item2;
    private Item item3;

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

        itemRequest1 = ItemRequest.builder()
                .description("request1")
                .requestor(user1)
                .created(LocalDateTime.now())
                .build();

        itemRequest2 = ItemRequest.builder()
                .description("request2")
                .requestor(user2)
                .created(LocalDateTime.now())
                .build();

        itemRequest3 = ItemRequest.builder()
                .description("request3")
                .requestor(user3)
                .created(LocalDateTime.now())
                .build();
        itemRequest1 = itemRequestRepository.save(itemRequest1);
        itemRequest2 = itemRequestRepository.save(itemRequest2);
        itemRequest3 = itemRequestRepository.save(itemRequest3);

        item1 = Item.builder()
                .name("item1")
                .description("item1")
                .available(true)
                .owner(user1)
                .request(itemRequest2)
                .build();
        item2 = Item.builder()
                .name("item2")
                .description("item2")
                .available(false)
                .owner(user2)
                .request(itemRequest3)
                .build();
        item3 = Item.builder()
                .name("item3")
                .description("item3")
                .available(true)
                .owner(user3)
                .request(itemRequest1)
                .build();
        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);
        item3 = itemRepository.save(item3);
    }

    @Test
    void shouldFindAllByOwnerIdOrderByIdAsc() {
        item3.setOwner(user1);
        item3 = itemRepository.save(item3);

        Collection<Item> items = itemRepository.findAllByOwnerIdOrderByIdAsc(user1.getId(),
                pageRequest).getContent();
        log.info("items = {}", items);
        assertNotNull(items);
        assertEquals(2, items.size());
        assertIterableEquals(List.of(item1, item3), items);
    }

    @Test
    void shouldSearchAvailableByNameOrDescriptionContainingIgnoreCase() {
        item3.setDescription("iTEm12");
        item3 = itemRepository.save(item3);
        Collection<Item> items = itemRepository.searchAvailableByNameOrDescriptionContainingIgnoreCase("TEM1",
                pageRequest).getContent();
        log.info("items = {}", items);
        assertNotNull(items);
        assertEquals(2, items.size());
        assertIterableEquals(List.of(item1, item3), items);
    }

    @Test
    void shouldFindAllByRequestIn() {
        Collection<Item> items = itemRepository.findAllByRequestIn(List.of(itemRequest1, itemRequest3));
        log.info("items = {}", items);
        assertNotNull(items);
        assertEquals(2, items.size());
        assertIterableEquals(List.of(item3, item2), items);
    }

    @Test
    void shouldFindAllByRequestId() {
        Collection<Item> items = itemRepository.findAllByRequestId(itemRequest3.getId());
        log.info("items = {}", items);
        assertNotNull(items);
        assertEquals(1, items.size());
        assertIterableEquals(List.of(item2), items);
    }
}