package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

@Transactional(readOnly = true)
public interface ItemService {
    public Collection<ItemDto> getAllUserItems(long userId);

    public ItemDto getItemById(long itemId);

    @Transactional
    public ItemDto addNewItem(long userId, ItemDto itemDto);

    @Transactional
    public ItemDto editItem(long userId, long itemId, ItemDto itemDto);

    public Collection<ItemDto> searchItems(String text);
}
