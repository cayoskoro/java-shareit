package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Collection<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User getById(long id) {
        return repository.findById(id);
    }

    @Override
    public User create(User user) {
        checkUniqueUserByEmail(user);
        return repository.create(user);
    }

    @Override
    public User update(User user) {
        checkUniqueUserByEmail(user);
        return repository.update(user);
    }

    @Override
    public User delete(long id) {
        return repository.delete(id);
    }

    private void checkUniqueUserByEmail(User user) {
        User existingUserByEmail = repository.findByEmail(user.getEmail());
        if (existingUserByEmail != null && existingUserByEmail.getId() != user.getId()) {
            log.info("Данный email - {} уже существует", user.getEmail());
            throw new ConflictException(String.format("Данный email - %s уже существует", user.getEmail()));
        }
    }
}
