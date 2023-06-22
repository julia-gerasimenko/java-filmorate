package ru.yandex.practicum.filmorate.storage;

public interface LikeDao {
    void createLike(long id, long userId);

    void deleteLike(long id, long userId);
}
