package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.likes.LikesStorage;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    private final LikesStorage likesStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       UserService userService, LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
        this.likesStorage = likesStorage;
    }


    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public List<Film> getFilmsList() {
        return new ArrayList<>(filmStorage.getFilms());
    }

    public Film getFilmById(int id) {
        Film film = filmStorage.getFilmById(id);
        if (film != null) {
            return film;
        } else {
            log.error("Нет такого фильма Id = {}", id);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Нет такого фильма Id = " + id);
        }
    }

    public void like(int filmId, int userId) {
        userService.getUserById(userId);
        likesStorage.addLike(filmId, userId);
    }

    public void unlike(int filmId, int userId) {
        userService.getUserById(userId);
        likesStorage.removeLike(filmId, userId);
    }

    public List<Film> getMoviesByLikes(int count) {
        return filmStorage.getPopularFilms(count);
    }

}