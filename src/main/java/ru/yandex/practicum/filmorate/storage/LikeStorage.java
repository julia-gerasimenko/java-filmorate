package ru.yandex.practicum.filmorate.storage;

public interface LikeStorage {
    void createLike(int id, int userId);

    void deleteLike(int id, int userId);
}
