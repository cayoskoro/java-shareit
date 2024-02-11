package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.UnsupportedStateException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponseDto getBookingById(long userId, long bookingId) {
        checkIfUserExists(userId);

        Booking booking = getBookingByIdOrElseThrow(bookingId);

        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            log.info("Пользователь по id = {} не является владельцом аренды и бронированной вещи - {}",
                    userId, booking);
            throw new NotFoundException(
                    String.format("Пользователь по id = %s не является владельцом аренды и бронированной вещи - %s",
                            userId, booking));
        }

        return mapper.convertToResponseDto(booking);
    }

    @Override
    public Collection<BookingResponseDto> getAllBookings(long userId, String stateStr, int from, int size) {
        State state = convertToStateOrElseThrow(stateStr);
        checkIfUserExists(userId);

        Collection<Booking> bookings;
        LocalDateTime currentTime = LocalDateTime.now();
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId, page).getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        currentTime, currentTime, page).getContent();
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, currentTime, page)
                        .getContent();
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, currentTime, page)
                        .getContent();
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING, page)
                        .getContent();
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED, page)
                        .getContent();
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + stateStr);
        }

        log.info("Список всех бронирований текущего пользователя = {} с параметром state = {}: {}", userId,
                state, bookings);
        return mapper.convertToResponseDtoCollection(bookings);
    }

    @Override
    public Collection<BookingResponseDto> getAllOwnerBookings(long userId, String stateStr, int from, int size) {
        State state = convertToStateOrElseThrow(stateStr);
        checkIfUserExists(userId);

        Collection<Booking> bookings;
        LocalDateTime currentTime = LocalDateTime.now();
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId, page).getContent();
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        currentTime, currentTime, page).getContent();
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, currentTime,
                        page).getContent();
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, currentTime,
                        page).getContent();
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING,
                        page).getContent();
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED,
                        page).getContent();
                break;
            default:
                throw new UnsupportedStateException("Unknown state: " + stateStr);
        }

        log.info("Список бронирований для всех вещей текущего пользователя = {} с параметром state = {}: {}", userId,
                state, bookings);
        return mapper.convertToResponseDtoCollection(bookings);
    }

    @Override
    @Transactional
    public BookingResponseDto addNewBooking(long userId, BookingRequestDto bookingRequestDto) {
        User user = getUserByIdOrElseThrow(userId);
        Item item = getItemByIdOrElseThrow(bookingRequestDto.getItemId());

        if (item.getOwner().getId() == userId) {
            log.info("Пользователь по id = {} является владельцем вещи по id = {}", userId, item.getId());
            throw new NotFoundException(String.format(
                    "Бронирование недоступно. Пользователь по id = %s является владельцем вещи по id = %s",
                    userId, item.getId()));
        }
        if (!item.isAvailable()) {
            log.info("Вещь по id = {} в данный момент недоступна", item.getId());
            throw new NotAvailableException(String.format("Вещь по id = %s в данный момент недоступна", item.getId()));
        }

        Booking booking = mapper.convertRequestDtoToEntity(bookingRequestDto);
        booking.setBooker(user);
        booking.setItem(item);
        return mapper.convertToResponseDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto approveBooking(long userId, long bookingId, Status status) {
        checkIfUserExists(userId);
        Booking booking = getBookingByIdOrElseThrow(bookingId);

        if (booking.getItem().getOwner().getId() != userId) {
            log.info("Пользователь по id = {} не является владельцом аренды - {}", userId, booking);
            throw new NotFoundException(String.format("Пользователь по id = %s не является владельцом аренды - %s",
                    userId, booking));
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            log.info("Аренда по id = {} уже одобрена владельцем по id = {}", userId, bookingId);
            throw new IncorrectParameterException(String.format(
                    "Аренда по id = %s уже одобрена владельцем по id = %s", userId, bookingId));
        }

        booking.setStatus(status);
        return mapper.convertToResponseDto(bookingRepository.save(booking));
    }

    private void checkIfUserExists(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
    }

    private Booking getBookingByIdOrElseThrow(long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Аренды по id = %s не существует", bookingId)));
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

    private State convertToStateOrElseThrow(String state) {
        try {
            return State.valueOf(state);
        } catch (Exception e) {
            throw new UnsupportedStateException("Unknown state: " + state);
        }
    }
}
