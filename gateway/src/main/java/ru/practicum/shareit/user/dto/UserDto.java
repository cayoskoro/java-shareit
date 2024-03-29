package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Value;
import ru.practicum.shareit.user.constraint.UserBase;
import ru.practicum.shareit.user.constraint.UserUpdate;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Value
@Builder(toBuilder = true)
public class UserDto {
    private final long id;
    @NotNull(groups = UserBase.class)
    private final String name;
    @NotNull(groups = UserBase.class)
    @Email(groups = {UserBase.class, UserUpdate.class})
    private final String email;
}
