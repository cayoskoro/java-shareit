package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    public List<ItemDto> getAllUserItems(long userId);
    public ItemDto getItemById(long itemId);
    public ItemDto addNewItem(long userId, ItemDto item);
    public ItemDto editItem(long userId, long itemId, ItemDto itemDto);
    public List<ItemDto> searchItems(String text);
}
