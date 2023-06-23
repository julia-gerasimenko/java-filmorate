package ru.yandex.practicum.filmorate.storage.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


@Component
@Slf4j
@RequiredArgsConstructor
public class FilmDaoImpl implements FilmDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film createFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name,description,release_date,duration,rating_id) VALUES (?,?,?,?,?)";
        KeyHolder id = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQuery, new String[]{"id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, id);
        film.setId(Objects.requireNonNull(id.getKey()).intValue());
        createFilmGenre(film);
        log.info("Добавлен новый фильм с id = {}", film.getId());
        return film;
    }

    private void createFilmGenre(Film film) {
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
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT f.*,  rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id";
        log.info("Получен список всех фильмов");
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        String sqlQuery = "SELECT f.*,  rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id WHERE f.id = ?";
        List<Film> film = jdbcTemplate.query(sqlQuery, this::makeFilm, id);
        if (film.size() == 0)
            return Optional.empty();
        log.info("Получен фильм с id = {}", id);
        return Optional.of(film.get(0));
    }

    @Override
    public List<Film> getFavoriteFilms(int id) {
        String sqlQuery = "SELECT f.*, rm.name AS mpa_name FROM films AS f " +
                "LEFT JOIN rating_mpa AS rm ON f.rating_id = rm.id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id " +
                "GROUP BY f.id ORDER BY COUNT(l.user_id) DESC LIMIT ?";
        log.info("Получен список любимых фильмов пользователя с id = {}", id);
        return jdbcTemplate.query(sqlQuery, this::makeFilm, id);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?," +
                "description = ?," +
                "release_date = ?," +
                "duration = ?," +
                "rating_id = ?" +
                "WHERE id = ?";
        int count = jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        if (count == 0) {
            throw new NotFoundException("Фильм не найден");
        }
        updateFilmGenre(film);
        log.info("Обновлен фильм с id = {}", film.getId());
        return film;
    }

    private void updateFilmGenre(Film film) {
        String sqlQueryGenres = "DELETE FROM film_genre WHERE film_id = ?";
        jdbcTemplate.update(sqlQueryGenres, film.getId());
        this.createFilmGenre(film);
        log.info("обновлены жанры фильма {}", film.getName());
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        Mpa mpa = new Mpa(rs.getInt("rating_id"), rs.getString("mpa_name"));
        return new Film(rs.getInt("id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                mpa,
                new LinkedHashSet<>()
        );
    }
}