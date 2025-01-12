package ru.yandex.practicum.filmorate.storage.friends;

import java.util.Set;

public interface FriendshipStorage {

    void addFriend(int userId, int friendId);

    Set<Integer> getUserFriends(int userId);

    void deleteFriend(int userId, int friendId);

}