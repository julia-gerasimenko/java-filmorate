package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

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
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isPresent()) {
            log.info("Получен фильм с id = {} ", filmId);
            return film.get();
        } else {
            log.error("Фильм с id =" + filmId + " не найден");
            throw new NotFoundException("Фильм с id =" + filmId + " не найден");
        }
    }

    public Film addLikes(int filmId, long userId) {
        if (!userStorage.getUserById(userId).isPresent()) {
            log.error("Пользователь с id = " + userId + "не существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь с id = " + userId +
                    "не существует");
        }
        Film film = getFilmById(filmId);
        if (!film.getLikes().contains((int) userId)) {
            film.getLikes().add((int) userId);
            log.info("Фильму с id = {} добавлен лайк", filmId);
            return film;
        } else {
            log.error("Пользователь не найден");
            throw new UserAlreadyExistException("Пользователь не найден");
        }
    }

    public Film removeLikes(int filmId, int userID) {
        if (filmId < 0 || userID < 0) {
            log.error("Отрицательное значение лайков не допустимо");
            throw new NotFoundException("Отрицательное значение лайков не допустимо");
        }
        Film film = getFilmById(filmId);
        film.getLikes().remove(userID);
        log.info("Пользователь с id = {} удалил лайк с фильма с id = {}", userID, filmId);
        return film;
    }

    public List<Film> favoritesFilms(Integer number) {
        log.info("Получены любимые фильмы");
        return filmStorage.getAllFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(film -> film.getLikes().size())))
                .limit(number)
                .collect(Collectors.toList());
    }
}
