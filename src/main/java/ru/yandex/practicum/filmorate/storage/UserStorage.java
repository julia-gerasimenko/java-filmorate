package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User createUser(User user);

    Optional<User> getUserById(Long id);

    List<User> getAllUsers();

    User updateUser(User user);

    // TODO снизу лишнее???

    User addFriend(Long id, Long friendId);

    User removeFriendById(long id, long friendId);

    List<Optional<User>> getAllFriends(long id);

    List<Optional<User>> getCommonFriends(long userId, long friendId);
}
