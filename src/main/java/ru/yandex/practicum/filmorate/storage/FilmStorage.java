package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film createFilm(Film film);

    Optional<Film> getFilmById(Integer id);

    List<Film> getAllFilms();

    Film updateFilm(Film film);
    List<Film> getFavoriteFilms(Integer number);
    Film removeLikes(int filmId, int userId);
    Film addLikes(int filmId, long userId);
}
