package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Успешно создан фильм с id = {}", film.getId());
        return filmService.createFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Всего {} фильмов", filmService.getAllFilms().size());
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Получен фильм с id = {} ", id);
        return filmService.getFilmById(id);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Фильм с id = {} {}", film.getId(), " был обновлен");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikes(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с id = {} {} {} ", userId, " поставил лайк фильму с id = ", id);
        filmService.addLikes(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikes(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь с id = {} {} {} ", userId, " удалил лайк у фильма id = ", id);
        filmService.removeLikes(id, userId);
    }


    @GetMapping("/popular")
    public List<Film> getTopFilms(@Positive @RequestParam(value = "count", defaultValue = "10") int count) {
        log.error("Список любимых фильмов получен");
        return filmService.favoritesFilms(count);
    }
}
