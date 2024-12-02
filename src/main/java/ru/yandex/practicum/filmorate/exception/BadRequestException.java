package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BadRequestException extends RuntimeException {

    public BadRequestException(HttpStatus status, String message) {
        super(message);
        log.error("Неправильный запрос {}: {}", status, message);
    }

}