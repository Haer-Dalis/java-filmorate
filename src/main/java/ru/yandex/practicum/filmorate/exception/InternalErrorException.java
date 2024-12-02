package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InternalErrorException extends RuntimeException {

    public InternalErrorException(HttpStatus status, String message) {
        super(message);
        log.error("Внутренняя ошибка {}: {}", status, message);
    }

}