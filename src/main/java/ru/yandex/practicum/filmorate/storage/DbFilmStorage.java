package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public interface DbFilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film get(int id);

    List<Film> getAll();
    void addLike(int filmId, int userId);
    void deleteLike(int filmId, int userId);

    List<Film> getTopTenFilms(int counts);
}

