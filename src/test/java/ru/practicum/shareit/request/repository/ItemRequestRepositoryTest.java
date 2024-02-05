package ru.practicum.shareit.request.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class ItemRequestRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRequestRepository itemRequestRepository;
    private final PageRequest pageRequest = PageRequest.of(0, 10);
    private User user1;
    private User user2;
    private User user3;
    private ItemRequest itemRequest1;
    private ItemRequest itemRequest2;
    private ItemRequest itemRequest3;

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
    }

    @Test
    void shouldFindAllByRequestorIdOrderByCreatedDesc() {
        itemRequest1.setCreated(LocalDateTime.now().minusMonths(3));
        itemRequest2.setCreated(LocalDateTime.now().minusMonths(1));
        itemRequest3.setCreated(LocalDateTime.now().minusMonths(2));
        itemRequest3.setRequestor(user2);
        itemRequest1 = itemRequestRepository.save(itemRequest1);
        itemRequest2 = itemRequestRepository.save(itemRequest2);
        itemRequest3 = itemRequestRepository.save(itemRequest3);

        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(
                user2.getId());
        log.info("itemRequests = {}", itemRequests);
        assertNotNull(itemRequests);
        assertEquals(2, itemRequests.size());
        assertIterableEquals(List.of(itemRequest2, itemRequest3), itemRequests);
    }

    @Test
    void shouldFindAllByRequestorIdNotOrderByCreatedDesc() {
        itemRequest1.setCreated(LocalDateTime.now().minusMonths(3));
        itemRequest2.setCreated(LocalDateTime.now().minusMonths(1));
        itemRequest3.setCreated(LocalDateTime.now().minusMonths(2));
        itemRequest3.setRequestor(user2);
        itemRequest1 = itemRequestRepository.save(itemRequest1);
        itemRequest2 = itemRequestRepository.save(itemRequest2);
        itemRequest3 = itemRequestRepository.save(itemRequest3);

        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdNotOrderByCreatedDesc(
                user2.getId(), pageRequest).getContent();
        log.info("itemRequests = {}", itemRequests);
        assertNotNull(itemRequests);
        assertEquals(1, itemRequests.size());
        assertIterableEquals(List.of(itemRequest1), itemRequests);
    }
}