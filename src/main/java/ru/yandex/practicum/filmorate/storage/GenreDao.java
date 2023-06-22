package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreDao {


    void createFilmGenre(Film film);

    Optional<Genre> getGenreById(int id);

    List<Genre> getAllGenres();

    void updateFilmGenre(Film film);

    void loadGenres(List<Film> films);
}
