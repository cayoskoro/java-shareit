package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService service;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemDto> getAllUserItems(@RequestHeader(HEADER_USER_ID) long userId) {
        return service.getAllUserItems(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId) {
        return service.getItemById(itemId);
    }

    @PostMapping
    public ItemDto addNewItem(@RequestHeader(HEADER_USER_ID) long userId, @Valid @RequestBody ItemDto itemDto) {
        return service.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto editItem(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId,
                            @RequestBody ItemDto itemDto) {
        return service.editItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchItems(@RequestHeader(HEADER_USER_ID) long userId, @RequestParam String text) {
        return service.searchItems(text);
    }

    @PostMapping("itemId/comment")
    public CommentDto addNewComment(@RequestHeader(HEADER_USER_ID) long userId, @RequestBody CommentDto commentDto) {
        return null;
    }
}
