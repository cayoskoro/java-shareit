package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final BookingMapper mapper;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingResponseDto getBookingById(long userId, long bookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Аренды по id = %s не существует", bookingId)));

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
    public Collection<BookingResponseDto> getAllBookings(long userId, State state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));

        Collection<Booking> bookings;
        LocalDateTime currentTime = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        currentTime, currentTime);
                break;
            case PAST:
                bookings = bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, currentTime);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, currentTime);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            default:
                throw new IncorrectParameterException(String.format(
                        "Введен некорректный параметр поиска state - %s", state));
        }

        log.info("Список всех бронирований текущего пользователя = {} с параметром state = {}: {}", userId,
                state, bookings);
        return bookings.stream()
                .map(mapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<BookingResponseDto> getAllOwnerBookings(long userId, State state) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));

        Collection<Booking> bookings;
        LocalDateTime currentTime = LocalDateTime.now();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId);
                break;
            case CURRENT:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId,
                        currentTime, currentTime);
                break;
            case PAST:
                bookings = bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, currentTime);
                break;
            case FUTURE:
                bookings = bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, currentTime);
                break;
            case WAITING:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.WAITING);
                break;
            case REJECTED:
                bookings = bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, Status.REJECTED);
                break;
            default:
                throw new IncorrectParameterException(String.format(
                        "Введен некорректный параметр поиска state - %s", state));
        }

        log.info("Список бронирований для всех вещей текущего пользователя = {} с параметром state = {}: {}", userId,
                state, bookings);
        return bookings.stream()
                .map(mapper::convertToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookingResponseDto addNewBooking(long userId, BookingRequestDto bookingRequestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new NotFoundException(
                        String.format("Вещи по id = %s не существует", bookingRequestDto.getItemId())));

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
    public BookingResponseDto approveBooking(long userId, long bookingId, Status status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException(String.format("Аренды по id = %s не существует", bookingId)));

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
}
