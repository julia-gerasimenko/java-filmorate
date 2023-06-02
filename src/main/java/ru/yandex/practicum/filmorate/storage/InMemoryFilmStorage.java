package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private int filmId = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film createFilm(Film film) {
        film.setId(filmId);
        films.put(filmId, film);
        filmId++;
        log.info("Создан фильм с id = {} ", film.getId());
        return film;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
            log.info("Фильм с id = {} был обновлен", film.getId());
        } else {
            log.error("Запрошенный фильм не существует");
            throw new NotFoundException("Запрошенный фильм не существует");
        }
        log.info("фильм с id = {} {}", id, "был успешно обновлен");
        return film;
    }

    @Override
    public List<Film> getFavoriteFilms(Integer number) {
        return getAllFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(film -> film.getLikes().size())))
                .limit(number)
                .collect(Collectors.toList());
    }

    @Override
    public Film removeLikes(int filmId, int userId) {
        if (filmId < 0 || userId < 0) {
            log.error("Отрицательное значение лайков не допустимо");
            throw new NotFoundException("Отрицательное значение лайков не допустимо");
        }
        if (getFilmById(filmId).isPresent()) {
            Film film = getFilmById(filmId).get();
            film.getLikes().remove(userId);
            log.info("Пользователь с id = {} удалил лайк с фильма с id = {}", userId, filmId);
            return film;
        } else {
            log.error("Фильм с запрашиваемым id {} не существует", filmId);
            throw new NotFoundException("Фильм с запрашиваемым id не существует");
        }
    }

    @Override
    public Film addLikes(int filmId, long userId) {
        if (getFilmById(filmId).isEmpty()) {
            log.error("Фильм с запрашиваемым id {} не существует", filmId);
            throw new NotFoundException("Фильм с запрашиваемым id не существует");
        }
        Film film = getFilmById(filmId).get();
        if (!film.getLikes().contains((int) userId)) {
            film.getLikes().add((int) userId);
            log.info("Фильму с id = {} добавлен лайк", filmId);
            return film;
        } else {
            log.error("Пользователь не найден");
            throw new UserAlreadyExistException("Пользователь не найден");
        }
    }
}
