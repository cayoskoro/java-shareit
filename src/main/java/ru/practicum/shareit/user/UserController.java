package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.constraint.UserBase;
import ru.practicum.shareit.user.constraint.UserUpdate;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping
    public Collection<UserDto> getAll() {
        return service.getAll();
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId) {
        return service.getById(userId);
    }

    @PostMapping
    public UserDto create(@Validated(UserBase.class) @RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @Validated(UserUpdate.class) @RequestBody UserDto userDto) {
        return service.update(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable long userId) {
        service.delete(userId);
    }
}
