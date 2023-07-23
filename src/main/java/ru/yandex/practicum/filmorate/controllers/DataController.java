package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Getter
@Slf4j
public class DataController {
    private final HashMap<Integer, Object> storage = new HashMap<>();
    private int count = 1;


    protected void add(Object obj) {
        if (obj.getClass().equals(Film.class)) {
            Film film = (Film) obj;
            if (film == null) throw new NotExpectedException("Невозможно сохранить пустой объект.");
            film.isValidate(film);
            if (storage.containsKey(film.getId()))
                throw new NotExpectedException("Фильм с id " +
                        film.getId() + " уже зарегистрирован.");
            film.setId(count);
            count++;
            storage.put(film.getId(), film);
            log.debug("Фильм " + film.getName() + " добавлен в библиотеку.");
        } else if (obj.getClass().equals(User.class)) {
            User user = (User) obj;
            if (user == null) throw new NotExpectedException("Невозможно сохранить пустой объект.");
            user.isValidate(user);
            if (storage.containsKey(user.getId()))
                throw new NotExpectedException("Пользователь с id " +
                        user.getId() + " уже зарегистрирован.");
            user.setId(count);
            count++;
            storage.put(user.getId(), user);
            log.debug("Пользователь " + user.getName() + " добавлен в систему пользователей.");
        } else {
            throw new NotExpectedException("Попытка добавить неизвестный объект.");
        }
    }

    protected void update(Object obj) {
        if (obj.getClass().equals(Film.class)) {
            Film film = (Film) obj;
            if (storage.containsKey(film.getId())) {
                film.isValidate(film);
                storage.put(film.getId(), film);
                log.debug("Фильм " + film.getName() + " обновлен.");
            } else {
                throw new NotExpectedException("Сначала добавьте фильм в библиотеку.");
            }
        } else if (obj.getClass().equals(User.class)) {
            User user = (User) obj;
            if (storage.containsKey(user.getId())) {
                user.isValidate(user);
                storage.put(user.getId(), user);
                log.debug("Данные пользователя " + user.getName() + " обновлены.");
            } else {
                throw new NotExpectedException("Сначала добавьте пользователя в систему.");
            }
        } else {
            throw new NotExpectedException("Попытка обновить неизвестный объект.");
        }
    }

    protected List<Film> getListOfFilms() {
        List<Film> films = new ArrayList<>();
        for (Object value : storage.values()) {
            if (value.getClass().equals(Film.class)) {
                films.add((Film) value);
            }
        }
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    protected List<User> getListOfUsers() {
        List<User> users = new ArrayList<>();
        for (Object value : storage.values()) {
            if (value.getClass().equals(User.class)) {
                users.add((User) value);
            }
        }
        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }
}