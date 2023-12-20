package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

public interface UserService {
    public Collection<User> getAll();
    public User getById(long id);
    public User create(User user);
    public User update(User user);
    public User delete(long id);
}
