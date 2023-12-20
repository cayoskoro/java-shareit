package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final List<Item> storage;

    @Override
    public List<ItemDto> getAllUserItems(long userId) {
        return null;
    }

    @Override
    public ItemDto getItemById(long itemId) {
        return null;
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        return null;
    }

    @Override
    public ItemDto editItem(long userId, long itemId, ItemDto itemDto) {
        return null;
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        return null;
    }
}
