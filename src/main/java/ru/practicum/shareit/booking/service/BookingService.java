package ru.practicum.shareit.booking.service;

import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BookingService {
}
