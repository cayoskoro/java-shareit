package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestMapper itemRequestMapper;
    private final ItemMapper itemMapper;
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    @Transactional
    public ItemRequestDto addNewItemRequest(long userId, ItemRequestDto itemRequestDto) {
        User requestor = getUserByIdOrElseThrow(userId);
        ItemRequest itemRequest = itemRequestMapper.convertToEntity(itemRequestDto);
        itemRequest.setRequestor(requestor);
        return itemRequestMapper.convertToDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public Collection<ItemRequestDto> getOwnerItemRequests(long userId) {
        checkIfUserExists(userId);
        Collection<ItemRequest> itemRequests = itemRequestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);
        Map<Long, List<Item>> itemMap = itemRepository.findAllByRequestIn(itemRequests).stream()
                .collect(Collectors.groupingBy(it -> it.getRequest().getId()));
        return itemRequests.stream()
                .map(itemRequest -> {
                    Collection<Item> items = itemMap.getOrDefault(itemRequest.getId(), new ArrayList<>());
                    return itemRequestMapper.convertToDto(itemRequest).toBuilder()
                            .items(itemMapper.convertToResponseDtoCollection(items)).build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemRequestDto> getAllItemRequests(long userId, int from, int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size, Sort.by("created").descending());
        Collection<ItemRequest> itemRequests = itemRequestRepository
                .findAllByRequestorIdNotOrderByCreatedDesc(userId, page).getContent();
        Map<Long, List<Item>> itemMap = itemRepository.findAllByRequestIn(itemRequests).stream()
                .collect(Collectors.groupingBy(it -> it.getRequest().getId()));
        return itemRequests.stream()
                .map(itemRequest -> {
                    Collection<Item> items = itemMap.getOrDefault(itemRequest.getId(), new ArrayList<>());
                    return itemRequestMapper.convertToDto(itemRequest).toBuilder()
                            .items(itemMapper.convertToResponseDtoCollection(items)).build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getItemRequest(long userId, long requestId) {
        checkIfUserExists(userId);
        ItemRequest itemRequest = getItemRequestByIdOrElseThrow(requestId);
        Collection<Item> items = itemRepository.findAllByRequestId(requestId);
        return itemRequestMapper.convertToDto(itemRequest).toBuilder()
                .items(itemMapper.convertToResponseDtoCollection(items))
                .build();
    }

    private void checkIfUserExists(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
    }

    private User getUserByIdOrElseThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
    }

    private ItemRequest getItemRequestByIdOrElseThrow(long requestId) {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Запрос вещи по id = %s не существует", requestId)));
    }
}
