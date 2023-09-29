package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    //Collection<UserDto> getAllUsers();
    List<UserDto> getAllUsers();

    UserDto getUserById(long userId);

    UserDto updateUser(long userId, UserDto userDto);

    void deleteUser(long userId);
}