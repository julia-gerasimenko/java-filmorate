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
        return filmService.createFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikes(@PathVariable int id, @PathVariable int userId) {
        filmService.addLikes(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikes(@PathVariable int id, @PathVariable int userId) {
        filmService.removeLikes(id, userId);
    }


    @GetMapping("/popular")
    public List<Film> getTopFilms(@Positive @RequestParam(value = "count", defaultValue = "10") int count) {
        return filmService.getFavoriteFilms(count);
    }
}
