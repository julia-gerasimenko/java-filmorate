package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.implementations.FriendDaoImpl;
import ru.yandex.practicum.filmorate.storage.implementations.UserDaoImpl;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserDaoImpl userStorage;
    private final FriendDaoImpl friendStorage;

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


    public void addFriend(Long id, Long friendId) {
        try {
            friendStorage.addFriend(id, friendId);
        } catch (Exception ex) {
            throw new NotFoundException("User or friend not found");
        }
        log.info("Друг с id = {} {} {}", friendId, " был добавлен пользователю с id = ", id);
        log.info("Друг с id = {} {} {}", id, " был добавлен пользователю с id = ", friendId);
    }

    public void removeFriendById(long id, long friendId) {
        log.info("Друг с id = {}{}{}", friendId, " был удален из друзей у пользователя с id = ", id);
        friendStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriends(long id) {
        log.info("Получено всего {} друзей у пользователя с id = {}", friendStorage.getAllFriends(id).size(), id);
        return friendStorage.getAllFriends(id);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        log.info("Получены общие друзья пользователей с  id = {}, {}", userId, friendId);
        return friendStorage.getCommonFriends(userId, friendId);
    }

    public void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
