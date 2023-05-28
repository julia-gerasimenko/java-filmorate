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
        Optional<User> user = userStorage.getUserById(id);
        if (user.isPresent()) {
            log.info("Получен пользователь с id = {}", id);
            return user.get();
        } else {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }


    public void addFriend(Long id, Long friendId) {
        User user = getUserById(id);
        if (user.getFriends().contains(friendId)) {
            log.info("Пользователь с id = {} {} {}", friendId, " был добавлен в друзья к пользователю с id = {}", id);
            throw new ValidationException(HttpStatus.BAD_REQUEST, "User " + id + " and the user " + friendId +
                    "have been friends yet ");
        }
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("Пользователь с id = {} {} {}", friendId, " был добавлен в друзья к пользователю с id = ", id);
        log.info("Пользователь с id = {} {} {}", id, " был добавлен в друзья к пользователю с id = ", friendId);
    }

    public void removeFriendById(long id, long friendId) {
        User user = getUserById(id);
        log.info("Пользователь с id = {}{}{}", friendId, " был удален из друзей у пользователей с id = ", id);
        user.getFriends().remove(friendId);
    }

    public List<User> getAllFriends(long id) {
        List<User> allFriends = getUserById(id).getFriends()
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
        log.info("Получено всего {} друзей у пользователя с id = {}", allFriends.size(), id);
        return allFriends;
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        log.info("Получены общие друзья пользователей с id = {} и id = {}", userId, friendId);
        List<User> friends = getAllFriends(userId);
        friends.retainAll(getAllFriends(friendId));
        return friends;
    }

    public void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
