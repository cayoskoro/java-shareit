package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";
    private final BookingService service;

    @PostMapping
    public BookingResponseDto addNewBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                            @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return service.addNewBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto approveBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                             @PathVariable long bookingId,
                                             @RequestParam(name = "approved") boolean isApproved) {
        Status status = isApproved ? Status.APPROVED : Status.REJECTED;
        return service.approveBooking(userId, bookingId, status);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingById(@RequestHeader(HEADER_USER_ID) long userId,
                                             @PathVariable long bookingId) {
        return service.getBookingById(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingResponseDto> getAllBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                         @RequestParam(required = false,
                                                                 defaultValue = "ALL") String state,
                                                         @RequestParam(defaultValue = "0") int from,
                                                         @RequestParam(defaultValue = "10") int size) {
        return service.getAllBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public Collection<BookingResponseDto> getAllOwnerBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                              @RequestParam(required = false,
                                                                      defaultValue = "ALL") String state,
                                                              @RequestParam(defaultValue = "0") int from,
                                                              @RequestParam(defaultValue = "10") int size) {
        return service.getAllOwnerBookings(userId, state, from, size);
    }
}
