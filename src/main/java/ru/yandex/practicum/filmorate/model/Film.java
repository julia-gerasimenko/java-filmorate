package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.FilmReleaseDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Builder
@AllArgsConstructor
@Data
public class Film {
    private int id;

    @NotBlank(message = "название фильма не может быть пустым")
    private String name;

    @NotBlank(message = "название фильма не может быть пустым")
    @Size(max = 200, message = "максимальная длина описания фильма — 200 символов")
    private String description;

    @FilmReleaseDate(message = "дата релиза — не раньше 28 декабря 1895 года")
    private LocalDate releaseDate;

    @NotNull(message = "продолжительность фильма не может быть пустой")
    @Positive(message = "продолжительность фильма должна быть положительной")
    private Integer duration;

    @Valid
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;

    public void addGenre(Genre genre) {
        genres.add(genre);
    }
}
