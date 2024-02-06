package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.ItemBaseTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
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
class CommentRepositoryTest extends ItemBaseTest {
    @Autowired
    private CommentRepository commentRepository;
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

        comment1 = commentRepository.save(comment1);
        comment2 = commentRepository.save(comment2);
        comment3 = commentRepository.save(comment3);
    }

    @Test
    void shouldFindAllByItemInEager() {
        Collection<Comment> comments = commentRepository.findAllByItemInEager(List.of(item1));
        log.info("comments = {}", comments);
        assertNotNull(comments);
        assertEquals(2, comments.size());
        assertIterableEquals(List.of(comment2, comment3), comments);
    }

    @Test
    void shouldFindAllByItemIdEager() {
        Collection<Comment> comments = commentRepository.findAllByItemIdEager(item2.getId());
        log.info("comments = {}", comments);
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertIterableEquals(List.of(comment1), comments);
    }
}