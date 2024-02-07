package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.practicum.shareit.user.UserBaseTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"db.name=test"})
class UserServiceTest extends UserBaseTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserMapper userMapper;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    protected void setUp() {
        super.setUp();

        Mockito.when(userMapper.convertToEntity(Mockito.any(UserDto.class))).thenReturn(user1);
        Mockito.when(userMapper.convertToDto(Mockito.any(User.class))).thenReturn(userDto1);
        Mockito.when(userMapper.clone(Mockito.any(User.class))).thenReturn(user1);
        Mockito.when(userMapper.convertToDtoCollection(Mockito.anyCollection()))
                .thenReturn(Collections.singletonList(userDto1));
    }

    @Test
    void shouldGetAllUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user1));
        Collection<UserDto> users = userService.getAll();
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals(users, List.of(userDto1));
        Mockito.verify(userRepository, Mockito.times(1)).findAll();
    }

    @Test
    void shouldGetUserById() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        UserDto userDto = userService.getById(user1.getId());
        assertNotNull(userDto);
        assertEquals(userDto, userDto1);
        Mockito.verify(userRepository, Mockito.times(1)).findById(user1.getId());
    }

    @Test
    void shouldCreateUser() {
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);
        UserDto userDto = userService.create(userDto1);
        assertNotNull(userDto);
        assertEquals(userDto, userDto1);
        Mockito.verify(userRepository, Mockito.times(1)).save(user1);
    }

    @Test
    void shouldUpdateUser() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user1);
        UserDto userDto = userService.update(user1.getId(), userDto1);
        assertNotNull(userDto);
        assertEquals(userDto, userDto1);
        Mockito.verify(userRepository, Mockito.times(1)).save(user1);
    }

    @Test
    void shouldDeleteUser() {
        Mockito.doNothing().when(userRepository).delete(Mockito.any(User.class));
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user1));
        userService.delete(user1.getId());
        Mockito.verify(userRepository, Mockito.times(1)).delete(Mockito.any());
    }
}