package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository extends JpaRepository<Item, Long> {
    public Collection<Item> findAllByOwnerIdOrderByIdAsc(long userId);

    @Query("select it " +
            "from Item as it " +
            "where it.available is true " +
            "and (upper(it.name) like concat('%', upper(?1), '%') " +
            "or upper(it.description) like concat('%', upper(?1), '%'))")
    public Collection<Item> searchAvailableByNameOrDescriptionContainingIgnoreCase(String text);
}
