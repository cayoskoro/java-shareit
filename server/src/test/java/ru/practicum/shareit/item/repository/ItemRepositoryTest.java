package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemBaseTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class ItemRepositoryTest extends ItemBaseTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);

        itemRequest1 = itemRequestRepository.save(itemRequest1);
        itemRequest2 = itemRequestRepository.save(itemRequest2);
        itemRequest3 = itemRequestRepository.save(itemRequest3);

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