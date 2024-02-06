package ru.practicum.shareit.request.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.ItemRequestBaseTest;
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
class ItemRequestRepositoryTest extends ItemRequestBaseTest {
    @Autowired
    private UserRepository userRepository;
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
    }

    @Test
    void shouldFindAllByRequestorIdOrderByCreatedDesc() {
        itemRequest1.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        itemRequest2.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(1));
        itemRequest3.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
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
        itemRequest1.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(3));
        itemRequest2.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(1));
        itemRequest3.setCreated(LOCAL_DATE_TIME_NOW_WITH_NANO.minusMonths(2));
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