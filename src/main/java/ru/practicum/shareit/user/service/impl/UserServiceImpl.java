package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ConflictException;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;

    @Override
    public Collection<UserDto> getAll() {
        return repository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователя по id = %s не существует", id)));
        log.info("Запрос пользователя по id = {} - {}", id, user);
        return mapper.convertToDto(user);
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = mapper.convertToEntity(userDto);
        checkUniqueUserByEmail(user);
        log.info("Добавлен новый пользователь - {}", user);
        return mapper.convertToDto(repository.save(user));
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
        User updatingUser = mapper.clone(user);
        mapper.updateUserFromDto(userDto, updatingUser);
        log.info("Пользователь подготовлен к обновлению - {}", updatingUser);
        checkUniqueUserByEmail(updatingUser);
        return mapper.convertToDto(repository.save(updatingUser));
    }

    @Override
    public void delete(long id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователя по id = %s не существует", id)));
        repository.delete(user);
        log.info("Удален пользователь - {}", user);
    }

    private void checkUniqueUserByEmail(User user) {
        User existingUserByEmail = repository.findByEmail(user.getEmail());
        if (existingUserByEmail != null && existingUserByEmail.getId() != user.getId()) {
            log.info("Данный email - {} уже существует", user.getEmail());
            throw new ConflictException(String.format("Данный email - %s уже существует", user.getEmail()));
        }
    }
}
