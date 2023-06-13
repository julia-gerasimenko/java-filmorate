package ru.yandex.practicum.filmorate.storage.implementations;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

public class GenreStorageImpl implements GenreStorage {
    @Override
    public void createFilmGenre(Film film) {

    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Genre> getAllGenres() {
        return null;
    }

    @Override
    public void updateFilmGenre(Film film) {

    }

    @Override
    public void loadGenres(List<Film> films) {

    }
}
