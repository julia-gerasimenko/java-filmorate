package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;
    private final LikeStorage likeStorage;

    public Film createFilm(Film film) {
        log.info("Фильм с id = {} создан", film.getId());
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Фильм с id = {} обновлен", film.getId());
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("Всего получено {} фильмов", filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int filmId) {

        Film film = filmStorage.getFilmById(filmId).orElseThrow(() ->
                new NotFoundException("Фильм с id =" + filmId + " не найден"));
        log.info("Получен фильм с id = {} ", filmId);
        return film;
    }

    public void addLikes(long filmId, long userId) {
        if (userStorage.getUserById(userId).isEmpty()) {
            log.error("Пользователь с id = " + userId + "не существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь с id = " + userId +
                    "не существует");
        }
        log.info("Пользователь с id = {} {} {} ", userId, " поставил лайк фильму с id = ", filmId);
        likeStorage.createLike(filmId, userId);
    }

    public void removeLikes(int filmId, int userId) {
        likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> getFavoriteFilms(Integer number) {
        log.info("Получены любимые фильмы");
        return filmStorage.getFavoriteFilms(number);
    }
}
