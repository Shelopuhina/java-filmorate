package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);
    User updateUser(User user);
    void deleteUser(User user);
    List<User> getUsers();
    Optional<User> getUserById(int id);
}
