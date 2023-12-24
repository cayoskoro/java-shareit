package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    public Collection<User> getAll();

    public User getById(long id);

    public User create(User user);

    public User update(User user);

    public User delete(long id);
}
