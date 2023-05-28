package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private long userId = 1;
    private final Map<Long, User> users = new HashMap<>();

    @Override
    public User createUser(User user) {
        user.setId(userId);
        users.put(userId, user);
        userId++;
        log.info("Пользователь с id = {} был успешно добавлен", user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        log.info("Получен пользователь с id = {}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Всего получено {} пользователей", users.values().size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User updateUser(User user) {
        long id = user.getId();
        if (users.containsKey(id)) {
            users.replace(id, user);
            log.info("Пользователь с id = {} был обновлен", user);
        } else {
            log.error("Запрашиваемый пользователь не существует");
            throw new NotFoundException("Запрашиваемый пользователь не существует");
        }
        log.info("пользователь с id = {} {}", id, "был обновлен");
        return user;
    }
}
