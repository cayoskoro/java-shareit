package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService service;
    private final ItemMapper mapper;

    @GetMapping
    public Collection<ItemDto> getAllUserItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return service.getAllUserItems(userId).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        return mapper.convertToDto(service.getItemById(itemId));
    }

    @PostMapping
    public ItemDto addNewItem(@RequestHeader("X-Sharer-User-Id") long userId, @Valid @RequestBody ItemDto itemDto) {
        Item item = mapper.convertToEntity(itemDto);
        return mapper.convertToDto(service.addNewItem(userId, item));
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable long itemId,
                            @RequestBody ItemDto itemDto) {
        Item existingItem = mapper.clone(service.getItemById(itemId));
        mapper.updateItemFromItemDto(itemDto, existingItem);
        log.info("Вещь подготовленная к обновлению - {}", existingItem);
        return mapper.convertToDto(service.editItem(userId, existingItem));
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestHeader("X-Sharer-User-Id") long userId, @RequestParam String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        } else if (!Pattern.matches("^[\\sа-яА-Яa-zA-Z0-9]+$", text)) {
            log.info("Передан некорректный параметр text = {}", text);
            throw new IncorrectParameterException(String.format("Передан некорректный параметр text = %s", text));
        }

        return service.searchItems(text).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }
}
