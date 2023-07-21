package ru.practicum.shareit.user.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository {
    private final Map<Long, User> users = new HashMap<>();
    private final HashSet<String> usersEmail = new HashSet<>();

    private long generatedId = 1;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteEmailInBase(String email) {
        usersEmail.remove(email);
    }

    @Override
    public User findUserById(long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь не найден.");
        }
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(generatedId++);
        users.put(user.getId(), user);
        usersEmail.add(user.getEmail());
        log.info("Добавлен новый пользователь с id = {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(long id, User user) {
        users.put(id, user);
        usersEmail.add(user.getEmail());
        log.info("Обновлен пользователь с id = {}", user.getId());
        return user;
    }

    @Override
    public void deleteUser(long id) {
        usersEmail.remove(users.get(id).getEmail());
        users.remove(id);
        log.info("Пользователь с id = {} удалён", id);
    }

    @Override
    public boolean isEmailPresentInRepository(User user) {
        return usersEmail.contains(user.getEmail());
    }
}