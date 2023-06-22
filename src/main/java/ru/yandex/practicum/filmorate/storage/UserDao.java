package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    User createUser(User user);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user);
}
