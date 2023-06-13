package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.FilmReleaseDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

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

    @JsonIgnore
    final Set<Integer> likes = new HashSet<>();

    @Valid
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;

    public void addGenre(Genre genre) {
        if (genres == null) {
            genres = new LinkedHashSet<>();
        }
        genres.add(genre);
    }
}
