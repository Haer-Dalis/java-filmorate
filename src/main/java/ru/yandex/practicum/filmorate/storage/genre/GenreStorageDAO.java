package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Objects;

@Component
@Qualifier("genreStorageDAO")
@RequiredArgsConstructor
public class GenreStorageDAO implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Integer> getAllGenreIds() {
        String sqlQuery = "SELECT id FROM genres";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class);
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sqlQuery = "SELECT * FROM genres WHERE id = ?";
        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> Genre.builder()
                                .id(rs.getInt("id"))
                                .name(rs.getString("genre"))
                                .build(),
                        id).stream()
                .findFirst()
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,
                        String.format("Жанр с id = %d не найден", id)));
    }

    @Override
    public boolean genreExists(Integer id) {
        String sqlQuery = "SELECT COUNT(*) FROM genres WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sqlQuery, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public List<Integer> getFilmGenreIds(int filmId) {
        String sqlQuery = "SELECT fg.genre_id FROM film_genres fg WHERE fg.film_id = ?";
        return jdbcTemplate.queryForList(sqlQuery, Integer.class, filmId);
    }

    @Override
    public void updateFilmGenres(Film film) {
        String deleteQuery = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteQuery, film.getId());

        if (Objects.nonNull(film.getGenres()) && !film.getGenres().isEmpty()) {
            String insertQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            for (Integer genreId : film.getGenres()) {
                if (!genreExists(genreId)) {
                    throw new NotFoundException(HttpStatus.NOT_FOUND,
                            String.format("Жанр с id = %d не найден", genreId));
                }
                jdbcTemplate.update(insertQuery, film.getId(), genreId);
            }
        }
    }
}