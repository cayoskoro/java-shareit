package ru.practicum.shareit.item.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> storage;
    private long counterId;

    @Override
    public Collection<Item> findAllUserItems(long id) {
        return storage.values().stream()
                .filter(it -> it.getOwner().getId() == id)
                .collect(Collectors.toList());
    }

    @Override
    public Item findById(long id) {
        Item item = storage.get(id);
        if (item == null) {
            log.info("Вещь по id = {} не существует", id);
            throw new NotFoundException(String.format("Вещь по id = %s не существует", id));
        }
        log.info("Запрос вещи по id = {} - {}", id, item);
        return item;
    }

    @Override
    public Item create(Item item) {
        Item createdItem = item.toBuilder()
                .id(generateId())
                .build();

        storage.put(createdItem.getId(), createdItem);
        log.info("Добавлена новая вещь - {}", createdItem);
        return createdItem;
    }

    @Override
    public Item update(Item item) {
        if (!storage.containsKey(item.getId())) {
            log.info("Обновляемая вещь с id = {} не существует", item.getId());
            throw new NotFoundException(String.format("Обновляемая вещь с id = %s не существует",
                    item.getId()));
        }
        storage.put(item.getId(), item);
        log.info("Обновлена вещь - {}", item);
        return item;
    }

    @Override
    public Collection<Item> searchItems(String text) {
        String lowerCaseText = text.toLowerCase();
        return storage.values().stream()
                .filter(it -> it.isAvailable() && (it.getName().toLowerCase().contains(lowerCaseText)
                        || it.getDescription().toLowerCase().contains(lowerCaseText)))
                .collect(Collectors.toList());
    }

    private long generateId() {
        return ++counterId;
    }
}
