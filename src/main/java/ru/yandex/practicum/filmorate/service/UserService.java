package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    private final FriendshipStorage friendshipStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendshipStorage friendshipStorage) {
        this.userStorage = userStorage;
        this.friendshipStorage = friendshipStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public List<User> getUsersList() {
        return new ArrayList<>(userStorage.getUsers());
    }

    public User getUserById(int id) {
        User user = userStorage.getUserById(id);
        if (user != null) {
            return user;
        } else {
            log.error("Нет такого юзера Id = {}", id);
            throw new NotFoundException(HttpStatus.NOT_FOUND, "Нет такого юзера Id = " + id);
        }
    }

    public void addFriend(int idUserOne, int idUserTwo) {
        friendshipStorage.addFriend(idUserOne, idUserTwo);
    }

    public void deleteFriend(int idUserOne, int idUserTwo) {
        friendshipStorage.deleteFriend(idUserOne, idUserTwo);
    }

    public List<User> getListOfFriends(int idUserOne) {
        return friendshipStorage.getUserFriends(idUserOne).stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getListOfMutualFriends(int idUserOne, int idUserTwo) {
        return getListOfFriends(idUserOne).stream()
                .filter(x -> getListOfFriends(idUserTwo).contains(x))
                .collect(Collectors.toList());
    }

}
