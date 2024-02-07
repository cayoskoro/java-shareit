package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService service;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemResponseDto> getAllUserItems(@RequestHeader(HEADER_USER_ID) long userId,
                                                       @RequestParam(defaultValue = "0") int from,
                                                       @RequestParam(defaultValue = "10") int size) {
        return service.getAllUserItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemResponseDto getItemById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId) {
        return service.getItemById(userId, itemId);
    }

    @PostMapping
    public ItemResponseDto addNewItem(@RequestHeader(HEADER_USER_ID) long userId,
                                      @RequestBody ItemRequestDto itemDto) {
        return service.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemResponseDto editItem(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId,
                                    @RequestBody ItemRequestDto itemDto) {
        return service.editItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public Collection<ItemResponseDto> searchItems(@RequestHeader(HEADER_USER_ID) long userId,
                                                   @RequestParam String text,
                                                   @RequestParam(defaultValue = "0") int from,
                                                   @RequestParam(defaultValue = "10") int size) {
        return service.searchItems(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addNewComment(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId,
                                    @RequestBody CommentDto commentDto) {
        return service.addNewComment(userId, itemId, commentDto);
    }
}
