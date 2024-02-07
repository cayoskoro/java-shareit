package ru.practicum.shareit.request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Collection;

@Transactional(readOnly = true)
public interface ItemRequestService {
    @Transactional
    public ItemRequestDto addNewItemRequest(long userId, ItemRequestDto itemRequestDto);

    public Collection<ItemRequestDto> getOwnerItemRequests(long userId);

    public Collection<ItemRequestDto> getAllItemRequests(long userId, int from, int size);

    public ItemRequestDto getItemRequest(long userId, long requestId);
}
