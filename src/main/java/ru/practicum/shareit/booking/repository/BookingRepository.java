package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    public Page<Booking> findAllByBookerId(long userId, Pageable page);

    public Page<Booking> findAllByBookerIdOrderByStartDesc(long userId, Pageable page);

    public Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                                    LocalDateTime start,
                                                                                    LocalDateTime end,
                                                                                    Pageable page);

    public Page<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end, Pageable page);

    public Page<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start,
                                                                        Pageable page);

    public Page<Booking> findAllByBookerIdAndStatusOrderByStartDesc(long userId, Status status, Pageable page);

    public Collection<Booking> findAllByItemOwnerId(long userId);

    public Collection<Booking> findAllByItemOwnerIdAndItemIn(long userId, Collection<Item> items);

    public Page<Booking> findAllByItemOwnerIdOrderByStartDesc(long userId, Pageable page);

    public Page<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId,
                                                                                       LocalDateTime start,
                                                                                       LocalDateTime end,
                                                                                       Pageable page);

    public Page<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime end,
                                                                          Pageable page);

    public Page<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime start,
                                                                           Pageable page);

    public Page<Booking> findAllByItemOwnerIdAndStatusOrderByStartDesc(long userId, Status status, Pageable page);

    public boolean existsByBookerIdAndItemIdAndStatusAndEndIsBeforeOrderByEndDesc(long userId, long itemId,
                                                                                  Status status, LocalDateTime end);
}
