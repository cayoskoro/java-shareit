package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
public class ItemRequestController {
    private final ItemRequestService service;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto addNewItemRequest(@RequestHeader(HEADER_USER_ID) long userId,
                                         @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return service.addNewItemRequest(userId, itemRequestDto);
    }

    @GetMapping
    public Collection<ItemRequestDto> getOwnerItemRequests(@RequestHeader(HEADER_USER_ID) long userId) {
        return service.getOwnerItemRequests(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllItemRequests(@RequestHeader(HEADER_USER_ID) long userId,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        return service.getAllItemRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequest(@RequestHeader(HEADER_USER_ID) long userId, @PathVariable long requestId) {
        return service.getItemRequest(userId, requestId);

    }
}
