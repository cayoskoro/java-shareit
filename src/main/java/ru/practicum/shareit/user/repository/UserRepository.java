package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    public Collection<User> findAll();

    public User findById(long id);

    public User create(User user);

    public User update(User user);

    public User delete(long id);

    public User findByEmail(String email);
}
