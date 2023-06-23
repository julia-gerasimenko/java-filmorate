package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDao;
import ru.yandex.practicum.filmorate.storage.GenreDao;
import ru.yandex.practicum.filmorate.storage.LikeDao;
import ru.yandex.practicum.filmorate.storage.UserDao;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final GenreDao genreDao;
    private final LikeDao likeDao;

    public Film create(Film film) {
        filmDao.createFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmDao.updateFilm(film);
        return film;
    }

    public List<Film> getAllFilms() {
        List<Film> films = filmDao.getAllFilms();
        genreDao.loadGenres(films);
        log.info("Всего получено {} фильмов", filmDao.getAllFilms().size());
        return films;
    }

    public Film getFilmById(int filmId) {

        Film film = filmDao.getFilmById(filmId).orElseThrow(() ->
                new NotFoundException("Фильм с id =" + filmId + " не найден"));
        genreDao.loadGenres(List.of(film));
        log.info("Получен фильм с id = {} ", filmId);
        return film;
    }

    public void addLikes(long filmId, long userId) {
        if (userDao.getUserById(userId).isEmpty()) {
            log.error("Пользователь с id = " + userId + "не существует");
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Пользователь с id = " + userId +
                    "не существует");
        }
        log.info("Пользователь с id = {} {} {} ", userId, " поставил лайк фильму с id = ", filmId);
        likeDao.createLike(filmId, userId);
    }

    public Film removeLikes(int filmId, int userId) {
        if (filmId < 0 || userId < 0) {
            throw new NotFoundException("Отрицательное значение id не допускается");
        }
        Film film = getFilmById(filmId);
        likeDao.deleteLike(filmId, userId);
        log.info("Пользователь с id = {} удалил лайк с фильма с id = {}", userId, filmId);
        return film;
    }

    public List<Film> getFavoriteFilms(Integer number) {
        log.info("Получены любимые фильмы");
        return filmDao.getFavoriteFilms(number);
    }
}
