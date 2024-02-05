package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Slf4j
class CommentRepositoryTest {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ItemRepository itemRepository;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private User user1;
    private User user2;
    private Item item1;
    private Item item2;

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

        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);

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

        item1 = itemRepository.save(item1);
        item2 = itemRepository.save(item2);

        comment1 = new Comment();
        comment1.setText("comment1");
        comment1.setItem(item2);
        comment1.setAuthor(user1);
        comment1.setCreated(LocalDateTime.now());
        comment2 = new Comment();
        comment2.setText("comment2");
        comment2.setItem(item1);
        comment2.setAuthor(user1);
        comment2.setCreated(LocalDateTime.now());
        comment3 = new Comment();
        comment3.setText("comment3");
        comment3.setItem(item1);
        comment3.setAuthor(user2);
        comment3.setCreated(LocalDateTime.now());

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