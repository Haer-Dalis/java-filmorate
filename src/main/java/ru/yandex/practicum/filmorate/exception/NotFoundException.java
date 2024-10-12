package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException {

    private final HttpStatus status;

    public NotFoundException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        log.error("Не найдено {}: {}", status, message);
    }

}