package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment as c join fetch c.author as a join fetch c.item as i where i in ?1")
    public Collection<Comment> findAllByItemInEager(Collection<Item> items);

    @Query("select c from Comment as c join fetch c.author as a join fetch c.item as i where i.id = ?1")
    public Collection<Comment> findAllByItemIdEager(long items);
}
