package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.common.exception.UnsupportedStateException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient client;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> addNewBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                                @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return client.addNewBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                                 @PathVariable long bookingId,
                                                 @RequestParam(name = "approved") boolean isApproved) {
        return client.approveBooking(userId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(HEADER_USER_ID) long userId,
                                                 @PathVariable long bookingId) {
        return client.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                 @RequestParam(required = false,
                                                         defaultValue = "ALL") String state,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        State stateEnum = State.from(state)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + state));
        return client.getAllBookings(userId, stateEnum, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllOwnerBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                      @RequestParam(required = false,
                                                              defaultValue = "ALL") String state,
                                                      @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size) {
        State stateEnum = State.from(state)
                .orElseThrow(() -> new UnsupportedStateException("Unknown state: " + state));
        return client.getAllOwnerBookings(userId, stateEnum, from, size);
    }
}
