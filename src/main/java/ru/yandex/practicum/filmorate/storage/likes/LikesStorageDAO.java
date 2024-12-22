package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

@Slf4j
@Component
@Qualifier("likesStorageDAO")
@RequiredArgsConstructor
public class LikesStorageDAO implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    /*@Override
    public void addLike(int userId, int filmId) {
        String sqlQuery = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, userId, filmId);
        log.info("пользователю с id {} нравится фильм с id {}", userId, filmId);
    } */

    @Override
    public void addLike(int userId, int filmId) {
        try {
            String checkFilmQuery = "SELECT COUNT(*) FROM films WHERE id = ?";
            Integer filmCount = jdbcTemplate.queryForObject(checkFilmQuery, Integer.class, filmId);

            if (filmCount == null || filmCount == 0) {
                throw new NotFoundException(HttpStatus.NOT_FOUND,
                        String.format("Фильм с id %d не найден", filmId));
            }
            String sqlQuery = "INSERT INTO likes (user_id, film_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, userId, filmId);
            log.info("пользователю с id {} нравится фильм с id {}", userId, filmId);
        } catch (NotFoundException e) {
            throw e;
        } catch (Exception e) {
            String errorMessage = String.format("Ошибка при добавлении лайка: userId=%d, filmId=%d. Причина: %s",
                    userId, filmId, e.getMessage());
            log.error(errorMessage, e);
            throw new InternalErrorException(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
        }
    }

    @Override
    public void removeLike(int userId, int filmId) {
        String sqlQuery = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
        jdbcTemplate.update(sqlQuery, userId, filmId);
    }

}