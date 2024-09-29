package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id;

    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            ++id;
            film.setId(id);
            films.put(id, film);
            log.info("Добавлен фильм: {}", film);
        } else {
            log.error("Не получилось добавить фильм ID {}", film.getId());
            throw new ValidationException("This Id already exists");
        }
        return film;
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Обновлен фильм с ID {}", film.getId());
            return film;
        } else {
            log.error("Не могу обновить ID {}", film.getId());
            throw new ValidationException("There is no such Id");
        }
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
