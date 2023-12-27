package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    public Collection<Item> findAllUserItems(long id);

    public Item findById(long id);

    public Item create(Item item);

    public Item update(Item item);

    public Collection<Item> searchItems(String text);
}
