package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int userId, int filmId) {
        if (filmStorage.getFilmById(filmId).isPresent() || userStorage.getUserById(userId).isPresent())
            throw new NotExpectedException("Фильм или пользователь не найдены.");
        Optional<Film> film = filmStorage.getFilmById(filmId);
        film.get().getLikes().add(userId);
    }


    public void deleteLike(int userId, int filmId) {
        if (filmStorage.getFilmById(filmId).isPresent() || userStorage.getUserById(userId).isPresent())
            throw new NotExpectedException("Фильм или пользователь не найдены.");
        Optional<Film> film = filmStorage.getFilmById(filmId);
        film.get().getLikes().remove(userId);
    }

    public List<Film> getTopTenFilms() {
        Map<Integer, Integer> allTheLists = new HashMap<>();
        for (Film film : filmStorage.getFilms()) {
            allTheLists.put(film.getId(), film.getLikes().size());
        }
        List<Map.Entry<Integer, Integer>> entryList = new ArrayList<Map.Entry<Integer, Integer>>(allTheLists.entrySet());
        Collections.sort(entryList, new Comparator<Map.Entry<Integer, Integer>>() {
            @Override
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        List<Film> topTenFilms = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : entryList) {
            Film film = filmStorage.getFilmById(entry.getKey()).get();
            topTenFilms.add(film);
        }
        topTenFilms = topTenFilms.stream().limit(10).collect(Collectors.toList());
        return topTenFilms;
    }
}


