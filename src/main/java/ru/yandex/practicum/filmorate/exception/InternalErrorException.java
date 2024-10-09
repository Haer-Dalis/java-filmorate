package ru.yandex.practicum.filmorate.exception;

import org.springframework.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InternalErrorException extends RuntimeException {

    private final HttpStatus status;

    public InternalErrorException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        log.error("Внетренняя ошибка {}: {}", status, message);
    }

    public HttpStatus getStatus() {
        return this.status;
    }
}