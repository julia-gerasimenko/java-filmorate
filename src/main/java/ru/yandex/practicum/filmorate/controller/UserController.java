package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.*;


@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int userId = 1;

    @GetMapping
    public Collection<User> findAll() {
        log.error("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        for (User u : users.values()) {
            if (u.getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с заданным email уже существует");
            }
        }
        fixUserName(user);
        user.setId(userId);
        users.put(userId, user);
        userId++;
        log.info("Пользователь {} был успешно добавлен", user);
        return user;
    }

    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        int id = user.getId();
        if (users.containsKey(id)) {
            fixUserName(user);
            users.put(id, user);
            log.info("Пользователь {} был успешно обновлен", user);
        } else {
            log.warn("Пользователь {} не существует", user);
            throw new ValidationException("Пользователь не существует");
        }
        return user;
    }

    private void fixUserName(User user) {
        if (user.getName() == null || !StringUtils.hasText(user.getName())) {
            user.setName(user.getLogin());
        }
    }
}
