package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public Collection<Item> getAllUserItems(long userId) {
        userRepository.findById(userId);
        return itemRepository.findAllUserItems(userId);
    }

    @Override
    public Item getItemById(long itemId) {
        return itemRepository.findById(itemId);
    }

    @Override
    public Item addNewItem(long userId, Item item) {
        User user = userRepository.findById(userId);
        item.setOwner(user);
        return itemRepository.create(item);
    }

    @Override
    public Item editItem(long userId, Item item) {
        checkIfUserExists(userId);
        if (item.getOwner().getId() != userId) {
            log.info("Пользователь по id = {} не является владельцом вещи - {}", userId, item);
            throw new NotFoundException(String.format("Пользователь по id = %s не является владельцом вещи вещи - %s",
                    userId, item));
        }
        return itemRepository.update(item);
    }

    @Override
    public Collection<Item> searchItems(String text) {
        log.info("Осуществляется поиск по подстроке - {}", text);
        return itemRepository.searchItems(text);
    }

    private void checkIfUserExists(long userId) {
        userRepository.findById(userId);
    }
}
