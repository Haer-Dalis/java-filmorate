package ru.yandex.practicum.filmorate.storage.film;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.InternalErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Integer, Film> films = new HashMap<>();
    private int id;

    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    public Film addFilm(@Valid Film film) {
        if (!films.containsKey(film.getId())) {
            film.setId(++id);
            films.put(film.getId(), film);
            log.info("Добавлен фильм: {}", film);
        } else {
            log.error("Не смог добавить фильм id = {} Он уже существует!", film.getId());
            throw new InternalErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Не смог добавить фильм id = " + film.getId() + " Он уже существует!");
        }
        return film;
    }

    public Film updateFilm(@Valid Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм с ID {}", film.getId());
            return film;
        } else {
            log.error("Нет фильма с id = {}", film.getId());
            throw new NotFoundException(HttpStatus.NOT_FOUND,
                    "Нет фильма с id = " + film.getId());
        }
    }


    public Film getFilmById(int id) {
        return films.get(id);
    }

    public void deleteFilm(int id) {
        films.remove(id);
    }
}
