package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    public Collection<Item> getAllUserItems(long userId);

    public Item getItemById(long itemId);

    public Item addNewItem(long userId, Item item);

    public Item editItem(long userId, Item item);

    public Collection<Item> searchItems(String text);
}
