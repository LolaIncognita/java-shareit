package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> findAllUsers();

    UserDto findUserDtoById(long id);

    UserDto addUserDto(UserDto userDto);

    UserDto updateUserDto(long id, UserDto userDto);

    boolean deleteUser(long id);
}