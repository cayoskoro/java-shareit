package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.util.Constants;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemRequestDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemMapper itemMapper;
    private final CommentMapper commentMapper;
    private final BookingMapper bookingMapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public Collection<ItemResponseDto> getAllUserItems(long userId, int from, int size) {
        checkIfUserExists(userId);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        Collection<Item> items = itemRepository.findAllByOwnerIdOrderByIdAsc(userId, page).getContent();
        Collection<Booking> bookings = bookingRepository.findAllByItemOwnerIdAndItemIn(userId, items);
        Map<Long, List<Comment>> commentMap = commentRepository.findAllByItemInEager(items).stream()
                .collect(Collectors.groupingBy(it -> it.getItem().getId()));

        return items.stream()
                .map(item -> {
                    Booking lastBooking = bookings.stream()
                            .filter(it -> it.getItem().getId().equals(item.getId())
                                    && isPastApprovedBooking(it))
                            .max(Comparator.comparing(Booking::getStart))
                            .orElse(null);
                    Booking nextBooking = bookings.stream()
                            .filter(it -> it.getItem().getId().equals(item.getId())
                                    && isFutureApprovedBooking(it))
                            .min(Comparator.comparing(Booking::getStart))
                            .orElse(null);
                    Collection<Comment> comments = commentMap.getOrDefault(item.getId(), new ArrayList<>());

                    return itemMapper.convertToResponseDto(item).toBuilder()
                            .lastBooking(bookingMapper.convertToResponseShortDto(lastBooking))
                            .nextBooking(bookingMapper.convertToResponseShortDto(nextBooking))
                            .comments(commentMapper.convertToDtoCollection(comments)).build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public ItemResponseDto getItemById(long userId, long itemId) {
        Item item = getItemByIdOrElseThrow(itemId);
        Collection<Booking> bookings = item.getOwner().getId() == userId
                ? bookingRepository.findAllByItemOwnerId(userId) : new ArrayList<>();
        Map<Long, List<Comment>> commentMap = commentRepository.findAllByItemIdEager(itemId).stream()
                .collect(Collectors.groupingBy(it -> it.getItem().getId()));

        Booking lastBooking = bookings.stream()
                .filter(this::isPastApprovedBooking)
                .max(Comparator.comparing(Booking::getStart))
                .orElse(null);
        Booking nextBooking = bookings.stream()
                .filter(this::isFutureApprovedBooking)
                .min(Comparator.comparing(Booking::getStart))
                .orElse(null);
        Collection<Comment> comments = commentMap.getOrDefault(item.getId(), new ArrayList<>());

        return itemMapper.convertToResponseDto(item).toBuilder()
                .lastBooking(bookingMapper.convertToResponseShortDto(lastBooking))
                .nextBooking(bookingMapper.convertToResponseShortDto(nextBooking))
                .comments(commentMapper.convertToDtoCollection(comments)).build();
    }

    @Override
    @Transactional
    public ItemResponseDto addNewItem(long userId, ItemRequestDto itemDto) {
        User user = getUserByIdOrElseThrow(userId);
        ItemRequest itemRequest = null;
        if (itemDto.getRequestId() != null) {
            itemRequest = itemRequestRepository.findById(itemDto.getRequestId())
                    .orElseThrow(() -> new NotFoundException(
                            String.format("Запроса вещи по id = %s не существует", itemDto.getRequestId())));
            if (itemRequest.getRequestor().getId() == userId) {
                throw new NotAvailableException(
                        String.format("Пользователь по id = %d не может добавить вещь на запрос вещи по id = %d, " +
                                "так как является создателем этого запроса", userId, itemRequest.getId()));
            }
        }

        Item item = itemMapper.convertRequestDtoToEntity(itemDto);
        item.setOwner(user);
        item.setRequest(itemRequest);

        log.info("Добавлена вещь - {}", item);
        return itemMapper.convertToResponseDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemResponseDto editItem(long userId, long itemId, ItemRequestDto itemDto) {
        checkIfUserExists(userId);
        Item item = getItemByIdOrElseThrow(itemId);
        Item updatingItem = itemMapper.clone(item);
        itemMapper.updateItemFromItemRequestDto(itemDto, updatingItem);
        log.info("Вещь подготовленная к обновлению - {}", updatingItem);
        if (updatingItem.getOwner().getId() != userId) {
            log.info("Пользователь по id = {} не является владельцом вещи - {}", userId, updatingItem);
            throw new NotFoundException(String.format("Пользователь по id = %s не является владельцом вещи - %s",
                    userId, updatingItem));
        }

        log.info("Вещь по id = {} обновлена - {}", itemId, updatingItem);
        return itemMapper.convertToResponseDto(itemRepository.save(updatingItem));
    }

    @Override
    public Collection<ItemResponseDto> searchItems(String text, int from, int size) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        } else if (!Constants.getAlphaNumericPattern().matcher(text).matches()) {
            log.info("Передан некорректный параметр text = {}", text);
            throw new IncorrectParameterException(String.format("Передан некорректный параметр text = %s", text));
        }
        log.info("Осуществляется поиск по подстроке - {}", text);
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemMapper.convertToResponseDtoCollection(
                itemRepository.searchAvailableByNameOrDescriptionContainingIgnoreCase(text, page).getContent());
    }

    @Override
    @Transactional
    public CommentDto addNewComment(long userId, long itemId, CommentDto commentDto) {
        User user = getUserByIdOrElseThrow(userId);
        Item item = getItemByIdOrElseThrow(itemId);
        if (!bookingRepository.existsByBookerIdAndItemIdAndStatusAndEndIsBeforeOrderByEndDesc(userId, itemId,
                Status.APPROVED, LocalDateTime.now())) {
            log.info("Добавление комментария к вещи по id = {} для пользователя по id = {} недоступно, так как " +
                    "отсутствует бронь", itemId, userId);
            throw new NotAvailableException(String.format(
                    "Добавление комментария к вещи по id = %s для пользователя по id = %s недоступно, так как " +
                            "отсутствует бронь", itemId, userId));
        }

        Comment comment = commentMapper.convertToEntity(commentDto);
        comment.setAuthor(user);
        comment.setItem(item);

        log.info("Пользователем по id = {} для вещи по id = {} добавлен комментарий - {}", userId, itemId, comment);
        return commentMapper.convertToDto(commentRepository.save(comment));
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

    private Item getItemByIdOrElseThrow(long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Вещи по id = %s не существует", itemId)));
    }

    private boolean isPastApprovedBooking(Booking booking) {
        return booking.getStatus().equals(Status.APPROVED) && booking.getStart().isBefore(LocalDateTime.now());
    }

    private boolean isFutureApprovedBooking(Booking booking) {
        return booking.getStatus().equals(Status.APPROVED) && booking.getStart().isAfter(LocalDateTime.now());
    }
}
