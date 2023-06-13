package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.implementations.FriendStorageImpl;
import ru.yandex.practicum.filmorate.storage.implementations.UserStorageImpl;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserStorageImpl userStorage;
    private final FriendStorageImpl friendStorage;

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
        User user = getUserById(id);
        if (user.getFriends().contains(friendId)) {
            log.info("Пользователь с id " + id
                    + " и пользователь с id " + friendId + "уже являются друзьями");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь с id " + id
                    + " и пользователь с id " + friendId + "уже являются друзьями");
        }

        if (user.getIncomingFriendRequests().contains(friendId)) {
            friendStorage.addFriend(id, friendId);
        } else {
            friendStorage.addFriendRequest(id, friendId);
        }
    }

    public void removeFriendById(long id, long friendId) {
        friendStorage.deleteFriend(id, friendId);
    }

    public List<User> getAllFriends(long id) {
        log.info("Получено всего {} друзей у пользователя с id = {}", friendStorage.getAllFriends(id).size(), id);
        return friendStorage.getAllFriends(id);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        return friendStorage.getCommonFriends(userId, friendId);
    }

    public void validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
