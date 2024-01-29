package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment AS c JOIN FETCH c.author AS a JOIN FETCH c.item AS i WHERE i IN ?1")
    public Collection<Comment> findAllByItemInEager(Collection<Item> items);

    @Query("SELECT c FROM Comment AS c JOIN FETCH c.author AS a JOIN FETCH c.item AS i WHERE i.id = ?1")
    public Collection<Comment> findAllByItemIdEager(long items);
}
