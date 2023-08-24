package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.DbUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final DbUserStorage<User> inMemoryStorage;

    @Autowired
    public UserService(DbUserStorage<User> inMemoryStorage) {
        this.inMemoryStorage = inMemoryStorage;
    }

    public User addUser(User user) {
        return inMemoryStorage.create(user);
    }

    public User updateUser(User user) {
        return inMemoryStorage.update(user);
    }

    public void addFriend(int userId, int friendId) {
        if ((getUserById(friendId)) == null || getUserById(userId) == null)
            throw new NotFoundException("Неверно указан id пользователя.");
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(int userId, int friendId) {
        if (getUserById(friendId) == null || getUserById(userId) == null)
            throw new NotFoundException("Пользователя с id " + friendId + "не существует.");
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(int userId) {
        List<Integer> friendsId = new ArrayList<>(getUserById(userId).getFriends());
        List<User> friends = new ArrayList<>();
        for (Integer integer : friendsId) {
            User user = inMemoryStorage.get(integer);
            friends.add(user);
        }
        return friends;
    }

    public List<User> getCommonFriends(int userId, int friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        List<Integer> friendOne = new ArrayList<>(user.getFriends());
        List<Integer> friendTwo = new ArrayList<>(friend.getFriends());
        List<Integer> fr = friendOne
                .stream()
                .filter(friendTwo::contains)
                .collect(Collectors.toList());
        List<User> friends = new ArrayList<>();
        for (Integer integer : fr) {
            User friend2 = inMemoryStorage.get(integer);
            friends.add(friend2);
        }
        return friends;
    }

    public User getUserById(int id) {
        return inMemoryStorage.get(id);
    }

    public List<User> getUsers() {
        return new ArrayList<>(inMemoryStorage.getAll());
    }

}
