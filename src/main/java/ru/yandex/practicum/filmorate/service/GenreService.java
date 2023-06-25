package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class GenreService {
    private final GenreDao genreDao;

    public List<Genre> getAllGenres() {
        log.info("Получен список всех жанров");
        return genreDao.getAllGenres();
    }

    public Genre getGenreById(Integer id) {
        log.info("Поиск жанра по id {}", id);
        Genre genre = genreDao.getGenreById(id)
                .orElseThrow(() -> new NotFoundException("Жанр с id {} не был найден" + id));
        log.info("Получен жанр по id = {}", id);
        return genre;
    }
}
