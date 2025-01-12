package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    User getUserById(int id);

    void deleteUser(int id);

    List<User> getUsers();

    void checkUser(int id);
}