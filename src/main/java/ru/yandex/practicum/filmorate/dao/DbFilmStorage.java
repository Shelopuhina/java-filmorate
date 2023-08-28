package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

public interface DbFilmStorage {
    Film create(Film film);

    Film update(Film film);

    Film getFilmById(int id);

    List<Film> getAllFilms();
    void addLike(int filmId, int userId);
    void deleteLike(int filmId, int userId);

    List<Film> getTopTenFilms(int counts);
    List<Genre> getFilmGenres(int filmId);
    Film addFilmGenres(Film film);
    void removeFilmGenres(int filmId);
}

