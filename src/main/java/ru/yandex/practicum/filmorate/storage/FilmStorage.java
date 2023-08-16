package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getFilms();

    void deleteFilm(Film film);

    Film getFilmById(int filmId);

    void addLike(int userId, int filmId);

    void deleteLike(int userId, int filmId);

    List<Film> getTopTenFilms(int count);
}
