package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository userRepository;

    @Override
    public Collection<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(long id) {
        User user = getUserByIdOrElseThrow(id);
        log.info("Запрос пользователя по id = {} - {}", id, user);
        return mapper.convertToDto(user);
    }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = mapper.convertToEntity(userDto);
        log.info("Добавлен новый пользователь - {}", user);
        return mapper.convertToDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(long userId, UserDto userDto) {
        User user = getUserByIdOrElseThrow(userId);
        User updatingUser = mapper.clone(user);
        mapper.updateUserFromDto(userDto, updatingUser);
        log.info("Пользователь подготовлен к обновлению - {}", updatingUser);
        return mapper.convertToDto(userRepository.save(updatingUser));
    }

    @Override
    @Transactional
    public void delete(long id) {
        User user = getUserByIdOrElseThrow(id);
        userRepository.delete(user);
        log.info("Удален пользователь - {}", user);
    }

    private User getUserByIdOrElseThrow(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователя по id = %s не существует", userId)));
    }
}
