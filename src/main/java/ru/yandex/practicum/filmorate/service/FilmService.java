package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
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
        getFilmById(filmId).getLikes().add(userId);
    }

    public void unlike(int filmId, int userId) {
        userService.getUserById(userId);
        getFilmById(filmId).getLikes().remove(userId);
    }

    public List<Film> getMoviesByLikes(int count) {
        List<Film> sortedList = new ArrayList<>(getFilmsList());
        sortedList.sort(new FilmsByLikesComparator());
        if (count == 0) {
            return sortedList.stream().limit(10).collect(Collectors.toList());
        } else {
            return sortedList.stream().limit(count).collect(Collectors.toList());
        }
    }

    private static class FilmsByLikesComparator implements Comparator<Film> {

        @Override
        public int compare(Film film1, Film film2) {
            return Integer.compare(film2.getLikes().size(), film1.getLikes().size());
        }
    }

}