package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface DbUserStorage {
    User create(User user);

    User update(User user);

    User getUserById(int id);

    List<User> getAllUsers();

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    List<Integer> getFriends(int userId);

    void deleteUser(int userId);
}

