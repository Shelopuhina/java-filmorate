package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface DbUserStorage {
    User create(User user);

    User update(User user);

    User get(int id);

    List<User> getAll();
    void addFriend(int userId,int friendId);
    void deleteFriend(int userId,int friendId);
    List<User> getFriends(int userId);
}

