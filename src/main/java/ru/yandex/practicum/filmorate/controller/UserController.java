package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    @PostMapping(value = "/users")
    public User addUser(@Valid @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            ++id;
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            user.setId(id);
            users.put(id, user);
            log.info("Добавлен новый юзер {}", user);
        } else {
            log.error("Не смог добавить юзера с ID {}", user.getId());
            throw new ValidationException("This Id already exists");
        }
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            if (user.getName() == null || user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Обновил юзера {}", user.getId());
            return user;
        } else {
            log.error("Не смог обновить юзера с ID {}", user.getId());
            throw new ValidationException("There is no such Id");
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
