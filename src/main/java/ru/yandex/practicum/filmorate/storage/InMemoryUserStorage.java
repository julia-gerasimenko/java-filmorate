package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public User addFriend(Long id, Long friendId) {
        if (getUserById(id).isEmpty() || getUserById(friendId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        User user = getUserById(id).get();
        User friend = getUserById(friendId).get();
        user.getFriends().add(friendId);
        friend.getFriends().add(id);

        log.info("Пользователь с id = {} {} {}", friendId, " был добавлен в друзья к пользователю с id = ", id);
        log.info("Пользователь с id = {} {} {}", id, " был добавлен в друзья к пользователю с id = ", friendId);

        return friend;
    }

    @Override
    public User removeFriendById(long id, long friendId) {
        if (getUserById(id).isEmpty() || getUserById(friendId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        User user = getUserById(id).get();
        User friend = getUserById(friendId).get();
        log.info("Пользователь с id = {}{}{}", friendId, " был удален из друзей у пользователей с id = ", id);
        user.getFriends().remove(friendId);
        return friend;
    }

    @Override
    public List<Optional<User>> getAllFriends(long id) {
        if (getUserById(id).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        User user = getUserById(id).get();
        List<Optional<User>> allFriends = user.getFriends()
                .stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
        return allFriends;
    }

    @Override
    public List<Optional<User>> getCommonFriends(long userId, long friendId) {
        log.info("Получены общие друзья пользователей с id = {} и id = {}", userId, friendId);
        List<Optional<User>> friends = getAllFriends(userId);
        friends.retainAll(getAllFriends(friendId));
        return friends;
    }
}
