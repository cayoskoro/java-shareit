package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.client.UserClient;
import ru.practicum.shareit.user.constraint.UserBase;
import ru.practicum.shareit.user.constraint.UserUpdate;
import ru.practicum.shareit.user.dto.UserDto;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserClient client;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return client.getAll();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable long userId) {
        return client.getById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@Validated(UserBase.class) @RequestBody UserDto userDto) {
        return client.create(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable long userId,
                                         @Validated(UserUpdate.class) @RequestBody UserDto userDto) {
        return client.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable long userId) {
        return client.delete(userId);
    }
}
