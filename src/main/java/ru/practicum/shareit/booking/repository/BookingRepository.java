package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    public Collection<Booking> findAllByBookerId(long userId);

    public Collection<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    public Collection<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                                             LocalDateTime start,
                                                                                             LocalDateTime end);

    public Collection<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    public Collection<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start);

    public Collection<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status status);

    public Collection<Booking> findAllByItemOwnerId(long userId);

    public Collection<Booking> findAllByItemOwnerIdOrderByStartDesc(long userId);

    public Collection<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                                             LocalDateTime start,
                                                                                             LocalDateTime end);

    public Collection<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end);

    public Collection<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start);

    public Collection<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status);
}
