package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.State;
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
    public BookingDto addNewBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                    @RequestBody @Valid BookingRequestDto bookingRequestDto) {
        return service.addNewBooking(userId, bookingRequestDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader(HEADER_USER_ID) long userId,
                                     @PathVariable long bookingId,
                                     @RequestParam(name = "approved") boolean isApproved) {
        return service.approveBooking(userId, bookingId, isApproved ? Status.APPROVED : Status.REJECTED);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(HEADER_USER_ID) long userId,
                                     @PathVariable long bookingId) {
        return service.getBookingById(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDto> getAllBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                 @RequestParam(required = false, defaultValue = "ALL") State state) {
        return service.getAllBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingDto> getAllOwnerBookings(@RequestHeader(HEADER_USER_ID) long userId,
                                                      @RequestParam(required = false,
                                                              defaultValue = "ALL") State state) {
        return service.getAllOwnerBookings(userId, state);
    }
}
