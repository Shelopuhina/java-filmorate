package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Slf4j
@Component
public class InMemoryUsersStorage implements UserStorage {
    private final HashMap<Integer, User> userStorage = new HashMap<>();
    private int count = 1;
    @Override
    public User addUser(User user) {
        if (user == null) throw new NotExpectedException("Невозможно сохранить пустой объект.");
        user.isValidate(user);
        if (userStorage.containsKey(user.getId()))
            throw new NotExpectedException("Объект с id " +
                    user.getId() + " уже зарегистрирован.");
        user.setId(count);
        count++;
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (userStorage.containsKey(user.getId())) {
            user.isValidate(user);
            userStorage.put(user.getId(), user);
            log.debug("Объект с id " + user.getId() + " обновлен.");
        } else {
            throw new NotExpectedException("Сначала добавьте объект в систему.");
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (user == null) throw new NotExpectedException("Невозможно удалить пустой объект.");
        if (userStorage.containsKey(user.getId())) {
            userStorage.remove(user.getId());
        }else{
            throw new NotExpectedException("Сначала добавьте объект в систему.");
        }
    }
    @Override
    public List<User> getUsers() {
        return new ArrayList<User>(userStorage.values());
    }

    @Override
    public Optional<User> getUserById(int id) {
        return userStorage.values().stream()
                .filter(x -> x.getId() == id)
                .findFirst();
    }
}
