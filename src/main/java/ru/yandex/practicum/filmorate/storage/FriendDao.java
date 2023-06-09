package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendDao {

    void addFriend(long id, long friendId);

    void deleteFriend(long id, long friendId);

    List<User> getCommonFriends(long id, long otherId);

    List<User> getAllFriends(long id);
}
