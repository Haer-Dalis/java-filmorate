package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotFoundException extends RuntimeException {

    public NotFoundException(HttpStatus status, String message) {
        super(message);
        log.error("Не найдено {}: {}", status, message);
    }

}