package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.InternalErrorException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int id;

    public User addUser(@Valid User user) {
        if (!users.containsKey(user.getId())) {
            attachNameIfEmpty(user);
            user.setId(++id);
            users.put(user.getId(), user);
            log.info("Добавлен новый юзер {}", user);
        } else {
            log.error("Не смог добавить юзера с ID {}", user.getId());
            throw new InternalErrorException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "id = " + user.getId() + " уже используется!");
        }
        return user;
    }

    public User updateUser(@Valid User user) {
        if (users.containsKey(user.getId())) {
            attachNameIfEmpty(user);
            users.put(user.getId(), user);
            log.info("Обновил юзера {}", user.getId());
            return user;
        } else {
            log.error("Не смог обновить юзера с ID {}", user.getId());
            throw new NotFoundException(HttpStatus.NOT_FOUND,
                    "Нет юзера с id = " + user.getId());
        }
    }

    @Override
    public void checkUser(int id) {
        if (id == 0 || !users.containsKey(id)) {
            throw new NotFoundException(HttpStatus.NOT_FOUND,
                    "Нет юзера с id = " + id);
        }
    }

    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

    private void attachNameIfEmpty(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

}
