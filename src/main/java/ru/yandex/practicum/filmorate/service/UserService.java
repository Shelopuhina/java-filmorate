package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.DbUserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final DbUserStorage userDb;
    int countUser = 1;

    @Autowired
    public UserService(DbUserStorage userDb) {
        this.userDb = userDb;
    }

    public User addUser(User user) {
        if (user == null) throw new NotFoundException("Невозможно сохранить пустой объект.");
        user.validate(user);
        user.setId(countUser);
        countUser++;
        return userDb.create(user);
    }

    public User updateUser(User user) {
        if (user == null) throw new NotFoundException("Невозможно обновить пустой объект.");
        try {
            user.validate(user);
            getUserById(user.getId());
            return userDb.update(user);
        }catch (NotFoundException exc) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    public void addFriend(int userId, int friendId) {
        if ((getUserById(friendId)) == null || getUserById(userId) == null)
            throw new NotFoundException("Неверно указан id пользователя.");
        User user = getUserById(userId);
        user.getFriends().add(friendId);
        userDb.addFriend(userId,friendId);
    }

    public void removeFriend(int userId, int friendId) {
        if (getUserById(friendId) == null || getUserById(userId) == null)
            throw new NotFoundException("Пользователь не найден.");
        User user = getUserById(userId);
        user.getFriends().remove(friendId);
        userDb.deleteFriend(userId,friendId);
    }

    public List<User> getFriends(int userId) {
           User user = getUserById(userId);
                   return user.getFriends().stream()
                           .map(userDb::getUserById)
                           .filter(Optional::isPresent)
                           .map(Optional::get)
                    .collect(Collectors.toList());
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
            User friend2 = getUserById(integer);
            friends.add(friend2);
        }
        return friends;
    }

    public User getUserById(int id) {
        if(userDb.getUserById(id).isEmpty()) throw new NotFoundException("Пользователь не найден.");
        return userDb.getUserById(id).get();

    }

    public List<User> getUsers() {
        return new ArrayList<>(userDb.getAllUsers());
    }

}
