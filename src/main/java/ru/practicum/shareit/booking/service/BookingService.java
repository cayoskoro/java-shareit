package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;

import java.util.Collection;

@Transactional(readOnly = true)
public interface BookingService {
    public BookingResponseDto getBookingById(long userId, long bookingId);

    public Collection<BookingResponseDto> getAllBookings(long userId, String stateStr);

    public Collection<BookingResponseDto> getAllOwnerBookings(long userId, String stateStr);

    @Transactional
    public BookingResponseDto addNewBooking(long userId, BookingRequestDto bookingRequestDto);

    @Transactional
    public BookingResponseDto approveBooking(long userId, long bookingId, Status status);
}
