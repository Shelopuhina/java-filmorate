package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Getter
@Slf4j
public class DataController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final HashMap<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    protected void addFilm(Film film) {
        if (film == null) throw new NotExpectedException("Невозможно сохранить пустой объект.");
        isValidate(film);
        films.put(nextId, film);
        nextId++;
    }

    protected void addUser(User user) {
        if (user == null) throw new NotExpectedException("Невозможно сохранить пустой объект.");
        isValidate(user);
        users.put(nextId, user);
        nextId++;
    }

    protected void updateFilm(Film film) {
        if (films.containsValue(film)) {
            isValidate(film);
            films.put(film.getId(), film);
        } else {
            throw new NotExpectedException("Сначала добавьте фильм в библиотеку.");
        }
    }

    protected void updateUser(User user) {
        if (users.containsValue(user)) {
            isValidate(user);
            users.put(user.getId(), user);
        } else {
            throw new NotExpectedException("Сначала добавьте фильм в библиотеку.");
        }
    }

    protected List<Film> getListOfFilms() {
        return new ArrayList<>(films.values());
    }

    protected List<User> getListOfUsers() {
        return new ArrayList<>(users.values());
    }

    private void isValidate(Object obj) {
        if (obj.getClass().equals(Film.class)) {
            Film film = (Film) obj;
            if (film.getDescription().length() > 200)
                throw new ValidationException("Фильм невозможно добавить. Описание фильма больше 200 символов.");
            if (film.getReleaseDate().isBefore(LocalDate.of(1995, 12, 28)))
                throw new ValidationException("Фильм невозможно добавить. Дата релиза фильма раньше 28.12.1995.");
            if (film.getDuration() < 0)
                throw new ValidationException("Фильм невозможно добавить. Продолжительность не может быть отрицательной.");
        } else if (obj.getClass().equals(User.class)) {
            User user = (User) obj;
            if (!user.getEmail().contains("@"))
                throw new ValidationException("Пользователя невозможно добавить. Email должен содержать - @.");
            if (user.getLogin().contains(" "))
                throw new ValidationException("Пользователя невозможно добавить. Login не должен содержать пробелы.");
            if (user.getName() == null)
                user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now()))
                throw new ValidationException("Пользователя невозможно добавить. День рождения должен быть указан до " + LocalDate.now());//проеврить вывод при эксепшене

        } else {
            throw new NotExpectedException("Попытка обновить неизвестный объект.");
        }
    }
}