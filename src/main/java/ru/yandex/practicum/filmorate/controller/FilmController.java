package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.InternalErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }


    @PostMapping(value = "/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping (value = "/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getFilmsList() {
        return filmService.getFilmsList();
    }

    @GetMapping("/films/{filmId}")
    public Film findFilmById(@PathVariable int filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping (value = "/films/{id}/like/{userId}")
    public void like(@PathVariable int id, @PathVariable int userId) {
        filmService.like(id, userId);
    }

    @DeleteMapping (value = "/films/{id}/like/{userId}")
    public void unlike(@PathVariable int id, @PathVariable int userId) {
        filmService.unlike(id, userId);
    }

    @GetMapping (value = "/films/popular")
    public List<Film> getMoviesByLikes(@RequestParam(required = false) Integer count) {
        if (count == null) {
            return filmService.getMoviesByLikes(10);
        } else {
            return filmService.getMoviesByLikes(count);
        }
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NotFoundException e) {
        return new ErrorResponse("Not found.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(final BadRequestException e) {
        return new ErrorResponse("Bad request.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handle(final InternalErrorException e) {
        return new ErrorResponse("Internal server error.");
    }

}
