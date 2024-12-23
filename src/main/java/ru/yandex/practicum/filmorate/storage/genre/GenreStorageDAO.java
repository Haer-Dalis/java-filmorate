package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Qualifier("genreStorageDAO")
@RequiredArgsConstructor
public class GenreStorageDAO implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM genres GROUP BY id";
        return jdbcTemplate.query(sqlQuery, GenreStorageDAO::buildGenre);
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sqlQuery = "SELECT * FROM genres g WHERE g.id = ?";
        return jdbcTemplate.query(sqlQuery, GenreStorageDAO::buildGenre, id).stream()
                .findAny().orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND,
                "не найден жанр с id" + id));
    }

    @Override
    public Set<Genre> getFilmGenres(int filmId) {
        String sqlQuery = "SELECT fg.genre_id id, g.genre FROM film_genres fg " +
                "LEFT JOIN genres g ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ? " +
                "ORDER BY fg.genre_id";
        Set<Genre> genres = jdbcTemplate.query(sqlQuery, GenreStorageDAO::buildGenre, filmId).stream()
                .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparingInt(Genre::getId))));
        return genres;
    }

    @Override
    public void updateFilmGenres(Film film) {
        String deleteQuery = "DELETE FROM film_genres WHERE film_id = ?";
        jdbcTemplate.update(deleteQuery, film.getId());
        List<Genre> sortedGenres = film.getGenres()
                .stream()
                .sorted(Comparator.comparingInt(Genre::getId))
                .toList();
        for (Genre genre : sortedGenres) {
            String sqlQuery = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
        }
    }

    private static Genre buildGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("genre"))
                .build();
    }

}