package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        validateUserName(user);
        log.info("Пользователь с id = {} успешно создан", user.getId());
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        validateUserName(user);
        log.info("Пользователь с id = {} был успешно обновлен", user.getId());
        return userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        log.info("Получено всего {} пользователей", userStorage.getAllUsers().size());
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        User user = userStorage.getUserById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
        log.info("Получен пользователь с id = {}", id);
        return user;
    }


    public User addFriend(Long id, Long friendId) {
        User user = getUserById(id);
        if (user.getFriends().contains(friendId)) {
            log.info("Пользователь с id " + id
                    + " и пользователь с id " + friendId + "уже являются друзьями");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь с id " + id
                    + " и пользователь с id " + friendId + "уже являются друзьями");
        }
        return userStorage.addFriend(id, friendId);
    }

    public User removeFriendById(long id, long friendId) {
        return userStorage.removeFriendById(id, friendId);
    }

    public List<Optional<User>> getAllFriends(long id) {
        log.info("Получено всего {} друзей у пользователя с id = {}", userStorage.getAllFriends(id).size(), id);
        return userStorage.getAllFriends(id);
    }

    public List<Optional<User>> getCommonFriends(long userId, long friendId) {
        return userStorage.getCommonFriends(userId,friendId);
    }

    public void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
