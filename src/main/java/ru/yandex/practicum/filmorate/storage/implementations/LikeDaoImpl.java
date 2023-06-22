package ru.yandex.practicum.filmorate.storage.implementations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.LikeDao;

@Component
@AllArgsConstructor
@Slf4j
public class LikeDaoImpl implements LikeDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void createLike(long id, long userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, id, userId);
        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, id);
    }

    @Override
    public void deleteLike(long id, long userId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, id, userId);
        log.info("Пользователь с id {} удалил лайк с фильма с id {}", userId, id);
    }


}
