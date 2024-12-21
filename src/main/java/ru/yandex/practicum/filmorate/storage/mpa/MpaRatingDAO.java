package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
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
                .findAny().orElseThrow(() -> new BadRequestException(HttpStatus.BAD_REQUEST,
                "Нет рейтинга с id = " + id));
    }

    @Override
    public void checkMpaRating(Integer id) {
        String sqlQuery = "SELECT COUNT(*) FROM mpa_rating WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);

        if (count == null || count == 0) {
            throw new NotFoundException(HttpStatus.NOT_FOUND,
                    String.format("Нет рейтинга с id %s", id));
        }
    }

    private static MpaRating buildMpaRating(ResultSet rs, int rowNum) throws SQLException {
        return MpaRating.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("rating_name"))
                .build();
    }

}

