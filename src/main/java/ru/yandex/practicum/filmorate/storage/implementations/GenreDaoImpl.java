package ru.yandex.practicum.filmorate.storage.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createFilmGenre(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
        String sql = "INSERT INTO film_genre (film_id, genre_id) " +
                "VALUES(?,?)";
        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, film.getId());
                        ps.setLong(2, genres.get(i).getId());
                    }

                    @Override
                    public int getBatchSize() {
                        return film.getGenres().size();
                    }
                });
        log.info("Создан жанр {} для фильма {}", film.getGenres(), film.getName());
    }

    @Override
    public Optional<Genre> getGenreById(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genre = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genre.size() == 0)
            return Optional.empty();
        log.info("Получен жанр по id = {}", id);
        return Optional.of(genre.get(0));
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genres";
        log.info("Получен список всех жанров");
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public void updateFilmGenre(Film film) {
        String sqlQueryGenres = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQueryGenres, film.getId());
        this.createFilmGenre(film);
        log.info("обновлены жанры фильма {}", film.getName());
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }

    @Override
    public void loadGenres(List<Film> films) {
        final Map<Integer, Film> ids = films.stream().collect(Collectors.toMap(Film::getId, Function.identity()));
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final String sqlQuery = "SELECT * from genres g, film_genre fg where fg.genre_id = g.id AND fg.film_id in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            if (!rs.wasNull()) {
                final Film film = ids.get(rs.getInt("FILM_ID"));
                film.addGenre(new Genre(rs.getInt("ID"), rs.getString("NAME")));
            }
        }, films.stream().map(Film::getId).toArray());
    }
}
