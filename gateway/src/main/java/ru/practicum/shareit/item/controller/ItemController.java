package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient client;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public ResponseEntity<Object> getAllUserItems(@RequestHeader(HEADER_USER_ID) long userId,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {
        return client.getAllUserItems(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId) {
        return client.getItemById(userId, itemId);
    }

    @PostMapping
    public ResponseEntity<Object> addNewItem(@RequestHeader(HEADER_USER_ID) long userId,
                                             @RequestBody @Valid ItemRequestDto itemDto) {
        return client.addNewItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> editItem(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId,
                                           @RequestBody ItemRequestDto itemDto) {
        return client.editItem(userId, itemId, itemDto);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestHeader(HEADER_USER_ID) long userId,
                                              @RequestParam String text,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        return client.searchItems(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addNewComment(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long itemId,
                                                @RequestBody @Valid CommentDto commentDto) {
        return client.addNewComment(userId, itemId, commentDto);
    }
}
