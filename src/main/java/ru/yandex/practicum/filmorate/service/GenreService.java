package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        log.info("Поиск жанра по id {}", id);
        Optional<Genre> genre = genreStorage.getGenreById(id);
        if (genre.isEmpty()) {
            throw new NotFoundException("Жанр с id {} не был найден" + id);
        }
        return genre.get();
    }

    public List<Genre> getGenres() {
        log.info("Получены все жанры");
        return genreStorage.getAllGenres();
    }
}
