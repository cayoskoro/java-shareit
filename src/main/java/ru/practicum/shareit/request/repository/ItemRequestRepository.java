package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {
    public Collection<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(long userId);

    public Page<ItemRequest> findAllByRequestorIdNotOrderByCreatedDesc(long userId, Pageable pageable);
}
