package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        User friend = getUserById(idUserTwo);
        getUserById(idUserOne).getFriends().add(idUserTwo);
        friend.getFriends().add(idUserOne);
    }

    public void deleteFriend(int idUserOne, int idUserTwo) {
        getUserById(idUserOne).getFriends().remove(idUserTwo);
        getUserById(idUserTwo).getFriends().remove(idUserOne);
    }

    public List<User> getListOfFriends(int idUserOne) {
        return setToList(getUserById(idUserOne).getFriends());
    }

    public List<User> getListOfMutualFriends(int idUserOne, int idUserTwo) {
        Set<Integer> userOneFriends = new HashSet<>(getUserById(idUserOne).getFriends());
        Set<Integer> userTwoFriends = new HashSet<>(getUserById(idUserTwo).getFriends());
        return userOneFriends.stream()
                .filter(userTwoFriends::contains)
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    private List<User> setToList(Set<Integer> friendsFromSet) {
        return friendsFromSet.stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

}
