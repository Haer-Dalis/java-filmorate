package ru.yandex.practicum.filmorate.storage.likes;

public interface LikesStorage {

    void addLike(int userId, int filmId);

    void removeLike(int userId, int filmId);

}
