package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class User {
    @NotNull
    private long id;
    @NotBlank
    private String name;
    @NotNull
    @Email
    private String email;
}
