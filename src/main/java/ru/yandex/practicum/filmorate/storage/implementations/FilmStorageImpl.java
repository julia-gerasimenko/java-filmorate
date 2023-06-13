package ru.yandex.practicum.filmorate.storage.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class FilmStorageImpl implements FilmStorage {

    @Override
    public Film createFilm(Film film) {
        return null;
    }

    @Override
    public Optional<Film> getFilmById(Integer id) {
        return Optional.empty();
    }

    @Override
    public List<Film> getAllFilms() {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public List<Film> getFavoriteFilms(Integer number) {
        return null;
    }
}
