package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    Optional getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user);
}
