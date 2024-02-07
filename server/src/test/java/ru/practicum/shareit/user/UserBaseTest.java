package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserBaseTest {
    protected User user1;
    protected UserDto userDto1;

    @BeforeEach
    protected void setUp() {
        user1 = User.builder()
                .id(1L)
                .name("user1")
                .email("user1@ya.ru")
                .build();
        userDto1 = UserDto.builder()
                .name(user1.getName())
                .email(user1.getEmail())
                .build();
    }
}
