package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id;

    @PostMapping(value = "/film")
    public Film addFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            validateFilm(film);
            ++id;
            film.setId(id);
            films.put(id, film);
        } else {
            throw new ValidationException("This Id already exists");
        }
        return film;
    }

    @PutMapping(value = "/film")
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validateFilm(film);
            films.put(film.getId(), film);
            return film;
        } else {
            throw new ValidationException("There is no such Id");
        }
    }

    @GetMapping("/film")
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER, 28))
                || film.getDuration() <= 0) {
            throw new ValidationException("Invalid film data");

        }
    }

}
