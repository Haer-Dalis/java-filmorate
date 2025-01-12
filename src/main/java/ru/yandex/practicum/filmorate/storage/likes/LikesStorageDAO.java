package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@Qualifier("likesStorageDAO")
@RequiredArgsConstructor
public class LikesStorageDAO implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addLike(int filmId, int userId) {
        try {
            String sqlQuery = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, filmId);
            log.info("Пользователю с id {} нравится фильм с id {}", userId, filmId);
        } catch (Exception e) {
            log.error("Ошибка при добавлении лайка: userId={}, filmId={}. Причина: {}", userId, filmId, e.getMessage());
            throw new RuntimeException("Ошибка при добавлении лайка", e);
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        try {
            String sqlQuery = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
            jdbcTemplate.update(sqlQuery, userId, filmId);
            log.info("Пользователь с id {} убрал лайк с фильма с id {}", userId, filmId);
        } catch (Exception e) {
            log.error("Ошибка при удалении лайка: userId={}, filmId={}. Причина: {}", userId, filmId, e.getMessage());
            throw new RuntimeException("Ошибка при удалении лайка", e);
        }
    }

    @Override
    public List<Integer> getLikes(int filmId) {
        String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        try {
            return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
        } catch (Exception e) {
            log.error("Ошибка при получении лайков для фильма с id {}. Причина: {}", filmId, e.getMessage());
            throw new RuntimeException("Ошибка при получении лайков для фильма", e);
        }
    }

}