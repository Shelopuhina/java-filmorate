package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.Film;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DbFilmStorage {
    Film create(Film film);

    Film update(Film film);

    Optional<Film> getFilmById(int id);

    List<Film> getAllFilms();
    void addLike(int filmId, int userId);
    void deleteLike(int filmId, int userId);

    List<Film> getTopTenFilms(int counts);
}

