package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmDao {
    Film createFilm(Film film);

    Optional<Film> getFilmById(int id);

    List<Film> getAllFilms();

    Film updateFilm(Film film);

    List<Film> getFavoriteFilms(int id);
}
