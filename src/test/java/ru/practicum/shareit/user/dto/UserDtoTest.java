package ru.practicum.shareit.user.dto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.UserBaseTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
class UserDtoTest extends UserBaseTest {
    private final JacksonTester<UserDto> json;

    @BeforeEach
    protected void setUp() {
        super.setUp();
    }

    @Test
    void shouldGetUserDto() throws Exception {

        log.info("userDto = {}", userDto1);
        JsonContent<UserDto> result = json.write(userDto1);
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo((int) userDto1.getId());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(userDto1.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(
                userDto1.getEmail());
    }

}