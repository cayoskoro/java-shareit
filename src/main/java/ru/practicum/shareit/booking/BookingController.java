package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService service;
    private static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @PostMapping
    public BookingDto addNewBooking(@RequestHeader(HEADER_USER_ID) long userId, @RequestBody BookingDto bookingDto) {
        return null;
    }

    @PatchMapping("/{bookingId}?")
    public BookingDto changeBookingState(@RequestHeader(HEADER_USER_ID) long userId,
                                         @RequestParam(name = "approved") boolean state) {
        return null;
    }

    @GetMapping
    public Collection<BookingDto> getAllBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                 @RequestParam(required = false, value = "ALL") String state) {
        return null;
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllOwnerBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                      @RequestParam(required = false, value = "ALL") String state) {
        return null;
    }
}
