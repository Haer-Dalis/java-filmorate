package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Qualifier("mpaRatingDAO")
@RequiredArgsConstructor
public class MpaRatingDAO implements MpaRatingStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<MpaRating> getAllMpaRatings() {
        String sqlQuery = "SELECT * FROM mpa_rating";
        return jdbcTemplate.query(sqlQuery, MpaRatingDAO::buildMpaRating);
    }

    @Override
    public MpaRating getMpaRatingById(Integer id) {
        String sqlQuery = "SELECT * FROM mpa_rating mpa WHERE mpa.id = ?";
        return jdbcTemplate.query(sqlQuery, MpaRatingDAO::buildMpaRating, id).stream()
                .findAny().orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,
                "Нет рейтинга с id = " + id));
    }

    @Override
    public void checkMpaRating(Integer id) {
        try {
            MpaRating rating = getMpaRatingById(id);
            if (rating == null) {
                throw new BadRequestException(HttpStatus.BAD_REQUEST,
                        String.format("не найден рейтинг с id %s", id));
            }
        } catch (EmptyResultDataAccessException e) {
            throw new BadRequestException(HttpStatus.BAD_REQUEST,
                    String.format("Ошибка СУБД - не найден рейтинг с id %s", id));
        }
    }

    private static MpaRating buildMpaRating(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("rating_name"))
                .build();
    }

}

