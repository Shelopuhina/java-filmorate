package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;
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
        if (films.containsKey(film.getId()))
            throw new NotExpectedException("Фильм с id " +
                    film.getId() + " уже зарегистрирован.");
        film.setId(nextId);
        films.put(film.getId(), film);
        log.debug("Фильм " + film.getName() + " добавлен в библиотеку.");
        nextId++;
    }

    protected void addUser(User user) {
        if (user == null) throw new NotExpectedException("Невозможно сохранить пустой объект.");
        isValidate(user);
        if (users.containsKey(user.getId()))
            throw new NotExpectedException("Пользователь с id " +
                    user.getId() + " уже зарегистрирован.");
        user.setId(nextId);
        users.put(user.getId(), user);
        log.debug("Пользователь " + user.getName() + " добавлен в систему пользователей.");
        nextId++;
    }

    protected void updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            isValidate(film);
            films.put(film.getId(), film);
            log.debug("Фильм " + film.getName() + " обновлен.");
        } else {
            throw new NotExpectedException("Сначала добавьте фильм в библиотеку.");
        }
    }

    protected void updateUser(User user) {
        if (users.containsKey(user.getId())) {
            isValidate(user);
            users.put(user.getId(), user);
            log.debug("Данные пользователя " + user.getName() + " обновлены.");
        } else {
            throw new NotExpectedException("Сначала добавьте пользователя в систему.");
        }
    }

    protected List<Film> getListOfFilms() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(films.values());
    }

    protected List<User> getListOfUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    public void isValidate(Object obj) {
        if (obj.getClass().equals(Film.class)) {
            Film film = (Film) obj;
            if (film.getName().isBlank())
                throw new ValidationException("Фильм невозможно добавить. Название фильма пустое.");
            if (film.getDescription().length() > 200)
                throw new ValidationException("Фильм невозможно добавить. Описание фильма больше 200 символов.");
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
                throw new ValidationException("Фильм невозможно добавить. Дата релиза фильма раньше 28.12.1995.");
            if (film.getDuration() < 0)
                throw new ValidationException("Фильм невозможно добавить. Продолжительность не может быть отрицательной.");
        } else if (obj.getClass().equals(User.class)) {
            User user = (User) obj;
            if (user.getEmail().isBlank())
                throw new ValidationException("Пользователя невозможно добавить. Email не должен быть пустым.");
            if (!user.getEmail().contains("@"))
                throw new ValidationException("Пользователя невозможно добавить. Email должен содержать - @.");
            if (user.getLogin().isBlank())
                throw new ValidationException("Пользователя невозможно добавить. Login не должен быть пустым.");
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