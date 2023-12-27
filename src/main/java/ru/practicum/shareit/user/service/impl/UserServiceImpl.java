package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ConflictException;
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
        return mapper.convertToDto(repository.findById(id));
    }

    @Override
    public UserDto create(UserDto userDto) {
        User user = mapper.convertToEntity(userDto);
        checkUniqueUserByEmail(user);
        return mapper.convertToDto(repository.create(user));
    }

    @Override
    public UserDto update(long userId, UserDto userDto) {
        User updatingUser = mapper.clone(repository.findById(userId));
        mapper.updateUserFromDto(userDto, updatingUser);
        log.info("Пользователь подготовлен к обновлению - {}", updatingUser);
        checkUniqueUserByEmail(updatingUser);
        return mapper.convertToDto(repository.update(updatingUser));
    }

    @Override
    public UserDto delete(long id) {
        return mapper.convertToDto(repository.delete(id));
    }

    private void checkUniqueUserByEmail(User user) {
        User existingUserByEmail = repository.findByEmail(user.getEmail());
        if (existingUserByEmail != null && existingUserByEmail.getId() != user.getId()) {
            log.info("Данный email - {} уже существует", user.getEmail());
            throw new ConflictException(String.format("Данный email - %s уже существует", user.getEmail()));
        }
    }
}
