package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.util.Constants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public Collection<ItemResponseDto> getAllUserItems(long userId) {
        checkIfUserExists(userId);
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(itemMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id = %s не существует", itemId)));
        log.info("Запрос вещи по id = {} - {}", itemId, item);
        return itemMapper.convertToResponseDto(item);
    }

    @Override
    public ItemResponseDto addNewItem(long userId, ItemRequestDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
        Item item = itemMapper.convertRequestDtoToEntity(itemDto);
        item.setOwner(user);
        return itemMapper.convertToResponseDto(itemRepository.save(item));
    }

    @Override
    public ItemResponseDto editItem(long userId, long itemId, ItemRequestDto itemDto) {
        checkIfUserExists(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id = %s не существует", itemId)));
        Item updatingItem = itemMapper.clone(item);
        itemMapper.updateItemFromItemRequestDto(itemDto, updatingItem);
        log.info("Вещь подготовленная к обновлению - {}", updatingItem);
        if (updatingItem.getOwner().getId() != userId) {
            log.info("Пользователь по id = {} не является владельцом вещи - {}", userId, updatingItem);
            throw new NotFoundException(String.format("Пользователь по id = %s не является владельцом вещи - %s",
                    userId, updatingItem));
        }
        return itemMapper.convertToResponseDto(itemRepository.save(updatingItem));
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        } else if (!Constants.getAlphaNumericPattern().matcher(text).matches()) {
            log.info("Передан некорректный параметр text = {}", text);
            throw new IncorrectParameterException(String.format("Передан некорректный параметр text = %s", text));
        }
        log.info("Осуществляется поиск по подстроке - {}", text);
        return itemRepository.searchAvailableByNameOrDescriptionContainingIgnoreCase(text).stream()
                .map(itemMapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addNewComment(long userId, long itemId, CommentDto commentDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id = %s не существует", itemId)));
        Comment comment = commentMapper.convertToEntity(commentDto);
        comment.setItem(item);
        return commentMapper.convertToDto(commentRepository.save(comment));
    }

    private void checkIfUserExists(long userId) {
        userRepository.findById(userId);
    }
}
