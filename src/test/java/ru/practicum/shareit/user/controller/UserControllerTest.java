package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@TestPropertySource(properties = "db.name=test")
class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    private UserDto userDto1;

    @BeforeEach
    void setUp() {
        userDto1 = UserDto.builder()
                .id(1L)
                .name("user1")
                .email("user1@ya.ru")
                .build();
    }

    @Test
    void shouldGetAllUsers() throws Exception {
        Mockito.when(userService.getAll()).thenReturn(Collections.singletonList(userDto1));

        mvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$[0].email", is(userDto1.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .getAll();
    }

    @Test
    void shouldGetUserById() throws Exception {
        Mockito.when(userService.getById(Mockito.anyLong())).thenReturn(userDto1);

        mvc.perform(get("/users/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .getById(1L);
    }

    @Test
    void shouldCreateUser() throws Exception {
        Mockito.when(userService.create(Mockito.any())).thenReturn(userDto1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .create(userDto1);
    }

    @Test
    void shouldUpdateUser() throws Exception {
        Mockito.when(userService.update(Mockito.anyLong(), Mockito.any())).thenReturn(userDto1);

        mvc.perform(patch("/users/{userId}", 1L)
                        .content(mapper.writeValueAsString(userDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName()), String.class))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail()), String.class));
        Mockito.verify(userService, Mockito.times(1))
                .update(1L, userDto1);
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/users/{userId}", 1L))
                .andExpect(status().isOk());
        Mockito.verify(userService, Mockito.times(1))
                .delete(1L);
    }
}