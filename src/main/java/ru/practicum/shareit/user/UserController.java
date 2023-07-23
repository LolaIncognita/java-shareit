package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
@Validated
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUserDtoById(@PathVariable long id) {
        return userService.findUserDtoById(id);
    }

    @PostMapping
    public UserDto addUserDto(@Valid @RequestBody UserDto userDto) {
        return userService.addUserDto(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto updateUserDto(@Valid @RequestBody UserDto userDto, @PathVariable long id) {
        return userService.updateUserDto(id, userDto);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
    }
}