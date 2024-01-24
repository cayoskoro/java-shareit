package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.util.Constants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
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
    private final ItemMapper mapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<ItemDto> getAllUserItems(long userId) {
        checkIfUserExists(userId);
        return itemRepository.findAllByOwnerId(userId).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id = %s не существует", itemId)));
        log.info("Запрос вещи по id = {} - {}", itemId, item);
        return mapper.convertToDto(item);
    }

    @Override
    public ItemDto addNewItem(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
        Item item = mapper.convertToEntity(itemDto);
        item.setOwner(user);
        return mapper.convertToDto(itemRepository.save(item));
    }

    @Override
    public ItemDto editItem(long userId, long itemId, ItemDto itemDto) {
        checkIfUserExists(userId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format("Вещь по id = %s не существует", itemId)));
        Item updatingItem = mapper.clone(item);
        mapper.updateItemFromItemDto(itemDto, updatingItem);
        log.info("Вещь подготовленная к обновлению - {}", updatingItem);
        if (updatingItem.getOwner().getId() != userId) {
            log.info("Пользователь по id = {} не является владельцом вещи - {}", userId, updatingItem);
            throw new NotFoundException(String.format("Пользователь по id = %s не является владельцом вещи вещи - %s",
                    userId, updatingItem));
        }
        return mapper.convertToDto(itemRepository.save(updatingItem));
    }

    @Override
    public Collection<ItemDto> searchItems(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        } else if (!Constants.getAlphaNumericPattern().matcher(text).matches()) {
            log.info("Передан некорректный параметр text = {}", text);
            throw new IncorrectParameterException(String.format("Передан некорректный параметр text = %s", text));
        }
        log.info("Осуществляется поиск по подстроке - {}", text);
        return itemRepository.searchAvailableByNameOrDescriptionContainingIgnoreCase(text).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    private void checkIfUserExists(long userId) {
        userRepository.findById(userId);
    }
}
