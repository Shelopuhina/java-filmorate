package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUsersStorage implements UserStorage {
    private final HashMap<Integer, User> userStorage = new HashMap<>();
    private int count = 1;

    @Override
    public User addUser(User user) {
        if (user == null) throw new NotFoundException("Невозможно сохранить пустой объект.");
        user.isValidate(user);
        if (userStorage.containsKey(user.getId()))
            throw new NotFoundException("Объект с id " +
                    user.getId() + " уже зарегистрирован.");
        userStorage.put(count, user);
        user.setId(count);
        count++;
        return user;
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public User updateUser(User user) {
        if (userStorage.containsKey(user.getId())) {
            user.isValidate(user);
            userStorage.put(user.getId(), user);
            log.debug("Объект с id " + user.getId() + " обновлен.");
        } else {
            throw new NotFoundException("Сначала добавьте объект в систему.");
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (user == null) throw new NotFoundException("Невозможно удалить пустой объект.");
        if (userStorage.containsKey(user.getId())) {
            userStorage.remove(user.getId());
        } else {
            throw new NotFoundException("Сначала добавьте объект в систему.");
        }
    }

    @Override
    public User getUserById(int id) {
        if (userStorage.get(id) == null) {
            throw new ValidationException("Пользователь с id " + id + "не найден.");
        }
        userStorage.get(id).isValidate(userStorage.get(id));
        return userStorage.get(id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        if ((getUserById(friendId)) == null || getUserById(userId) == null)
            throw new NotFoundException("Неверно указан id пользователя.");
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        if (getUserById(friendId) == null || getUserById(userId) == null)
            throw new NotFoundException("Пользователя с id " + friendId + "не существует.");
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    @Override
    public List<User> getFriends(int userId) {
        List<Integer> friendsId = new ArrayList<>(getUserById(userId).getFriends());
        List<User> friends = new ArrayList<>();
        for (Integer integer : friendsId) {
            User user = userStorage.get(integer);
            friends.add(user);
        }
        return friends;
    }

    @Override
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
            User friend2 = userStorage.get(integer);
            friends.add(friend2);
        }
        return friends;
    }
}


