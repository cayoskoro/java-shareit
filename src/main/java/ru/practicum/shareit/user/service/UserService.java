package ru.practicum.shareit.user.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Transactional(readOnly = true)
public interface UserService {
    public Collection<UserDto> getAll();

    public UserDto getById(long id);

    @Transactional
    public UserDto create(UserDto userDto);

    @Transactional
    public UserDto update(long id, UserDto userDto);

    @Transactional
    public void delete(long id);
}
