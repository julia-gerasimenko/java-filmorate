package ru.yandex.practicum.filmorate.storage.implementations;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendStorage;

import java.util.List;

public class FriendStorageImpl implements FriendStorage {
    @Override
    public void addFriend(long id, long friendId) {

    }

    @Override
    public void addFriendRequest(long id, long friendId) {

    }

    @Override
    public void deleteFriend(long id, long friendId) {

    }

    @Override
    public List<User> getCommonFriends(long id, long otherId) {
        return null;
    }

    @Override
    public List<User> getAllFriends(long id) {
        return null;
    }
}
