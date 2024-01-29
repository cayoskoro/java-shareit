package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public Collection<Item> findAllByOwnerIdOrderByIdAsc(long userId);

    @Query("SELECT it " +
            "FROM Item AS it " +
            "WHERE it.available IS TRUE " +
            "AND (UPPER(it.name) LIKE CONCAT('%', UPPER(?1), '%') " +
            "OR UPPER(it.description) LIKE CONCAT('%', UPPER(?1), '%'))")
    public Collection<Item> searchAvailableByNameOrDescriptionContainingIgnoreCase(String text);
}
