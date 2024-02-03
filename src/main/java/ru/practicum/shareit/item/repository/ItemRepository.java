package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public Page<Item> findAllByOwnerIdOrderByIdAsc(long userId, Pageable page);

    @Query("SELECT it " +
            "FROM Item AS it " +
            "WHERE it.available IS TRUE " +
            "AND (UPPER(it.name) LIKE CONCAT('%', UPPER(?1), '%') " +
            "OR UPPER(it.description) LIKE CONCAT('%', UPPER(?1), '%'))")
    public Page<Item> searchAvailableByNameOrDescriptionContainingIgnoreCase(String text, Pageable page);
}
