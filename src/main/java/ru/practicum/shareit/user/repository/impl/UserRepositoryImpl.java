package ru.practicum.shareit.user.repository.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Map;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> storage;
    private long counterId;

    @Override
    public Collection<User> findAll() {
        Collection<User> users = storage.values();
        log.info("Запрос всех пользователей - {}", users);
        return users;
    }

    @Override
    public User findById(long id) {
        User user = storage.get(id);
        if (user == null) {
            log.info("Пользователя по id = {} не существует", id);
            throw new NotFoundException(String.format("Пользователя по id = %s не существует", id));
        }
        log.info("Запрос пользователя по id = {} - {}", id, user);
        return user;
    }

    @Override
    public User create(User user) {
        User existingUserByEmail = findByEmail(user.getEmail());
        if (existingUserByEmail != null && existingUserByEmail.getId() != user.getId()) {
            log.info("Данный email - {} уже существует", user.getEmail());
            throw new ConflictException(String.format("Данный email - %s уже существует", user.getEmail()));
        }

        User createdUser = user.toBuilder()
                .id(generateId())
                .build();

        storage.put(createdUser.getId(), createdUser);
        log.info("Добавлен новый пользователь - {}", createdUser);
        return createdUser;
    }

    @Override
    public User update(User user) {
        if (!storage.containsKey(user.getId())) {
            log.info("Обновляемого пользователя с id = {} не существует", user.getId());
            throw new NotFoundException(String.format("Обновляемого пользователя с id = %s не существует",
                    user.getId()));
        }

        User existingUserByEmail = findByEmail(user.getEmail());
        if (existingUserByEmail != null && existingUserByEmail.getId() != user.getId()) {
            log.info("Данный email - {} уже существует", user.getEmail());
            throw new ConflictException(String.format("Данный email - %s уже существует", user.getEmail()));
        }

        storage.put(user.getId(), user);
        log.info("Обновлен пользователь - {}", user);
        return user;
    }

    @Override
    public User delete(long id) {
        User removedUser = storage.remove(id);
        log.info("Удален пользователь - {}", removedUser);
        return removedUser;
    }

    @Override
    public User findByEmail(String email) {
        return storage.values().stream()
                .filter(it -> it.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    private long generateId() {
        return ++counterId;
    }
}
