package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUsersStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        if (userStorage.getUserById(friendId).isPresent() || userStorage.getUserById(userId).isPresent()) throw new NotExpectedException("Неверно указан id пользователя.");
        Optional<User> user = userStorage.getUserById(userId);
        Optional<User> friend = userStorage.getUserById(friendId);
        user.get().getFriends().add(friendId);
        friend.get().getFriends().add(userId);
    }

    public void removeFriend(int userId, int friendId) {
        if (userStorage.getUserById(friendId).isPresent() && userStorage.getUserById(userId).isPresent()) {
            Optional<User> user = userStorage.getUserById(userId);
            Optional<User> friend = userStorage.getUserById(friendId);
            user.get().getFriends().remove(friendId);
            friend.get().getFriends().remove(userId);
        } else {
            throw new NotExpectedException("Пользователя с id " + friendId + "не существует.");
        }
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.getUserById(userId).get();
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int userId, int friendId) {
            User user = userStorage.getUserById(userId).get();
            User friend = userStorage.getUserById(friendId).get();
            return user.getFriends().stream().filter(id -> friend.getFriends().contains(id))
                    .map(userStorage::getUserById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
        }
    }
