package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

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
        log.info("Получен фильм с id = {}", id);
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Всего получено {} фильмов", films.values().size());
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
}
