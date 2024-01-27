package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;

import java.util.Collection;

@Transactional(readOnly = true)
public interface BookingService {
    public BookingDto getBookingById(long userId, long bookingId);
    public Collection<BookingDto> getAllBookings(long userId, State state);

    public Collection<BookingDto> getAllOwnerBookings(long userId, State state);

    @Transactional
    public BookingDto addNewBooking(long userId, BookingRequestDto bookingRequestDto);

    @Transactional
    public BookingDto approveBooking(long userId, long bookingId, Status status);
}
