package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {
private final Storage<Film> inMemoryStorage;
    @Autowired
    public FilmService() {
        this.inMemoryStorage = new InMemoryStorage<Film>();


    public void addLike(int id, int userId) {
        if (getFilmById(id) == null)
            throw new NotFoundException("Фильм не найден.");
        if (userId < 0) throw new NotFoundException("id не может быть отрицательным");
        Film film = getFilmById(id);
        film.validate(film);
        film.getLikes().add(userId);
    }

    public void deleteLike(int userId, int filmId) {
        if (getFilmById(filmId) == null)
            throw new NotFoundException("Фильмне найден.");
        if (userId < 0) throw new NotFoundException("id не может быть отрицательным");
        Film film = getFilmById(filmId);
        film.validate(film);
        film.getLikes().remove(userId);
    }

    public List<Film> getTopTenFilms(int counts) {
        if (counts < 0) throw new NotFoundException("Топ должен содержать больше 1 элемента.");
        return getFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(counts)
                .collect(Collectors.toList());
    }

    public Film addFilm(Film film) {
        return inMemoryStorage.create(film);
    }

    public Film updateFilm(Film film) {
        return inMemoryStorage.update(film);
    }

    public Film getFilmById(int id) {
        return inMemoryStorage.get(id);
    }

    public List<Film> getFilms() {
        return new ArrayList<>(inMemoryStorage.getAll());
    }
}


