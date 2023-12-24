package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.constraint.UserBase;
import ru.practicum.shareit.user.constraint.UserUpdate;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;
    private final UserMapper mapper;

    @GetMapping
    public Collection<UserDto> getAll() {
        return service.getAll().stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getById(@PathVariable long userId) {
        return mapper.convertToDto(service.getById(userId));
    }

    @PostMapping
    public UserDto create(@Validated(UserBase.class) @RequestBody UserDto userDto) {
        User user = mapper.convertToEntity(userDto);
        return mapper.convertToDto(service.create(user));
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable long userId, @Validated(UserUpdate.class) @RequestBody UserDto userDto) {
        User existingUser = mapper.clone(service.getById(userId));
        mapper.updateUserFromDto(userDto, existingUser);
        return mapper.convertToDto(service.update(existingUser));
    }

    @DeleteMapping("/{userId}")
    public UserDto delete(@PathVariable long userId) {
        return mapper.convertToDto(service.delete(userId));
    }
}
