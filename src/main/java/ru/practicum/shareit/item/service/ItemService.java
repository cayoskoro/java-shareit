package ru.practicum.shareit.item.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;

import java.util.Collection;

@Transactional(readOnly = true)
public interface ItemService {
    public Collection<ItemResponseDto> getAllUserItems(long userId);

    public ItemResponseDto getItemById(long itemId);

    @Transactional
    public ItemResponseDto addNewItem(long userId, ItemRequestDto itemDto);

    @Transactional
    public ItemResponseDto editItem(long userId, long itemId, ItemRequestDto itemDto);

    public Collection<ItemResponseDto> searchItems(String text);

    @Transactional
    public CommentDto addNewComment(long userId, long itemId, CommentDto commentDto);
}
