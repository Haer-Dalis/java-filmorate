package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

public interface GenreStorage {

    Genre getGenreById(Integer id);

    List<Integer> getAllGenreIds();

    boolean genreExists(Integer id);

    void updateFilmGenres(Film film);

    List<Integer> getFilmGenreIds(int filmId);

}
