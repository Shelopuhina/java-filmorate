package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(int userId, int filmId) {
        filmStorage.addLike(userId, filmId);
    }


    public void deleteLike(int userId, int filmId) {
        filmStorage.deleteLike(userId, filmId);
    }

    public List<Film> getTopTenFilms(int counts) {
        return filmStorage.getTopTenFilms(counts);
    }
}


