package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    public Collection<ItemDto> getAllUserItems(long userId);

    public ItemDto getItemById(long itemId);

    public ItemDto addNewItem(long userId, ItemDto itemDto);

    public ItemDto editItem(long userId, long itemId, ItemDto itemDto);

    public Collection<ItemDto> searchItems(String text);
}
