package ru.yandex.practicum.filmorate.storage.likes;

import java.util.List;

public interface LikesStorage {

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Integer> getLikes(int filmId);

}
