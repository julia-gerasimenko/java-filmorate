package ru.yandex.practicum.filmorate.storage.implementations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreDao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Genre> getGenreById(int id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        List<Genre> genre = jdbcTemplate.query(sqlQuery, this::makeGenre, id);
        if (genre.size() == 0) {
            return Optional.empty();
        }
        log.info("Получен жанр по id = {}", id);
        return Optional.of(genre.get(0));
    }

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genres";
        log.info("Получен список всех жанров");
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id"), rs.getString("name"));
    }
}
