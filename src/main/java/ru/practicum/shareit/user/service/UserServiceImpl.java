package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.EmailValidationException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAllUsers().stream().map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findUserDtoById(long id) {
        return UserMapper.toUserDto(userRepository.findUserById(id));
    }

    @Override
    public UserDto addUserDto(UserDto userDto) {
        validationBeforeAdd(userDto);
        if (userRepository.isEmailPresentInRepository(UserMapper.toUser(userDto))) {
            throw new EmailValidationException(userDto.getEmail());
        }
        return UserMapper.toUserDto(userRepository.addUser(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto updateUserDto(long id, UserDto userDto) {
        User user = validationBeforeUpdate(id, userDto);
        return UserMapper.toUserDto(userRepository.updateUser(id, user));
    }

    @Override
    public boolean deleteUser(long id) {
        return userRepository.deleteUser(id);
    }

    private void validationBeforeAdd(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new ValidationException("Email не должен быть пустым");
        }
        if (userDto.getName() == null) {
            throw new ValidationException("Имя пользователя не должно быть пустым");
        }

    }

    private User validationBeforeUpdate(long id, UserDto userDto) {
        User user = userRepository.findUserById(id);
        if (userDto.getName() != null) {
            if (userDto.getName().isBlank()) {
                throw new ValidationException("Имя пользователя не должно быть пустым");
            }
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            if (userDto.getEmail().isBlank()) {
                throw new EmailValidationException("Email не должен быть пустым");
            }
            if (!user.getEmail().equals(userDto.getEmail()) && userRepository.isEmailPresentInRepository(UserMapper
                    .toUser(userDto))) {
                throw new EmailValidationException(String.format("%s уже есть в базе у другого пользователя", userDto.getEmail()));
            }
            user.setEmail(userDto.getEmail());
        }
        return user;
    }
}
