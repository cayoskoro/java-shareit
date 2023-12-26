package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    public Collection<UserDto> getAll();

    public UserDto getById(long id);

    public UserDto create(UserDto userDto);

    public UserDto update(long id, UserDto userDto);

    public UserDto delete(long id);
}
