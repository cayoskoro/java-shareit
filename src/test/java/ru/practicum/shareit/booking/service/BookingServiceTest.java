package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.BookingBaseTest;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.common.exception.IncorrectParameterException;
import ru.practicum.shareit.common.exception.NotAvailableException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.UnsupportedStateException;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"db.name=test"})
class BookingServiceTest extends BookingBaseTest {
    @Autowired
    private BookingService bookingService;
    @MockBean
    private ItemRepository itemRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private BookingRepository bookingRepository;
    @MockBean
    private BookingMapper bookingMapper;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        Mockito.when(bookingMapper.convertToResponseDto(Mockito.any(Booking.class)))
                .thenReturn(bookingResponseDto1);
        Mockito.when(bookingMapper.convertToResponseDtoCollection(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(bookingResponseDto1));
        Mockito.when(bookingMapper.convertRequestDtoToEntity(Mockito.any(BookingRequestDto.class)))
                .thenReturn(booking1);
    }

    @Test
    void shouldGetBookingById() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking1));

        BookingResponseDto bookingResponseDto = bookingService.getBookingById(user1.getId(), booking1.getId());

        assertNotNull(bookingResponseDto);
        assertEquals(bookingResponseDto, bookingResponseDto1);
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findById(booking1.getId());
    }

    @Test
    void shouldGetAllBookingsWithStatusUnsupported() {
        final UnsupportedStateException exception = assertThrows(
                UnsupportedStateException.class,
                () -> bookingService.getAllBookings(user1.getId(), "UNSUPPORTED", 0, 10)
        );
    }

    @Test
    void shouldGetAllBookingsWithStatusALL() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByBookerIdOrderByStartDesc(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllBookings(user1.getId(),
                "ALL", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllBookingsWithStatusCURRENT() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class),
                                Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));


        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllBookings(user1.getId(),
                "CURRENT", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllBookingsWithStatusPAST() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByBookerIdAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllBookings(user1.getId(),
                "PAST", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllBookingsWithStatusFUTURE() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByBookerIdAndStartAfterOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllBookings(user1.getId(),
                "FUTURE", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllBookingsWithStatusWAITING() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllBookings(user1.getId(),
                "WAITING", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
        Mockito.verify(bookingRepository, Mockito.times(1))
                .findAllByBookerIdAndStatusOrderByStartDesc(user1.getId(),
                        Status.WAITING, PageRequest.of(0, 10));
    }

    @Test
    void shouldGetAllBookingsWithStatusREJECTED() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByBookerIdAndStatusOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllBookings(user1.getId(),
                "REJECTED", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllOwnerBookingsWithStatusUnsupported() {
        final UnsupportedStateException exception = assertThrows(
                UnsupportedStateException.class,
                () -> bookingService.getAllOwnerBookings(user1.getId(), "UNSUPPORTED", 0, 10)
        );
    }

    @Test
    void shouldGetAllOwnerBookingsWithStatusALL() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByItemOwnerIdOrderByStartDesc(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllOwnerBookings(user1.getId(),
                "ALL", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllOwnerBookingsWithStatusCURRENT() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class), Mockito.any(LocalDateTime.class),
                                Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllOwnerBookings(user1.getId(),
                "CURRENT", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllOwnerBookingsWithStatusPAST() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllOwnerBookings(user1.getId(),
                "PAST", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllOwnerBookingsWithStatusFUTURE() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(LocalDateTime.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllOwnerBookings(user1.getId(),
                "FUTURE", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllOwnerBookingsWithStatusWAITING() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllOwnerBookings(user1.getId(),
                "WAITING", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldGetAllOwnerBookingsWithStatusREJECTED() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository
                        .findAllByItemOwnerIdAndStatusOrderByStartDesc(Mockito.anyLong(),
                                Mockito.any(Status.class), Mockito.any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(booking1)));

        Collection<BookingResponseDto> bookingResponseDtos = bookingService.getAllOwnerBookings(user1.getId(),
                "REJECTED", 0, 10);

        assertNotNull(bookingResponseDtos);
        assertEquals(bookingResponseDtos, List.of(bookingResponseDto1));
    }

    @Test
    void shouldAddNewBooking() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        item1 = item1.toBuilder().owner(user1.toBuilder().id(2L).build()).build();
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking1);

        BookingResponseDto bookingResponseDto = bookingService.addNewBooking(user1.getId(), bookingRequestDto1);

        assertNotNull(bookingResponseDto);
        assertEquals(bookingResponseDto, bookingResponseDto1);
        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking1);
    }

    @Test
    void shouldAddNewBookingWithNotAvailable() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        item1 = item1.toBuilder().available(false).owner(user1.toBuilder().id(2L).build()).build();
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item1));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking1);

        final NotAvailableException exception = assertThrows(
                NotAvailableException.class,
                () -> bookingService.addNewBooking(user1.getId(), bookingRequestDto1)
        );
    }

    @Test
    void shouldApproveBooking() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        booking1.setStatus(Status.WAITING);
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking1));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking1);

        BookingResponseDto bookingResponseDto = bookingService.approveBooking(user1.getId(), booking1.getId(),
                Status.WAITING);

        assertNotNull(bookingResponseDto);
        assertEquals(bookingResponseDto, bookingResponseDto1);
        Mockito.verify(bookingRepository, Mockito.times(1)).save(booking1);
    }

    @Test
    void shouldApproveBookingWithIncorrectParameter() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking1));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking1);

        final IncorrectParameterException exception = assertThrows(
                IncorrectParameterException.class,
                () -> bookingService.approveBooking(user1.getId(), booking1.getId(), Status.WAITING)
        );

    }

    @Test
    void shouldApproveBookingWithNotFound() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        booking1.setStatus(Status.WAITING);
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(booking1));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking1);

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.approveBooking(2, booking1.getId(), Status.WAITING)
        );

    }

}