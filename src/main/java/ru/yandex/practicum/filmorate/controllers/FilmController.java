package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmController(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }
    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.debug("Получен запрос POST /films." + film.toString());
        filmStorage.addFilm(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
       filmStorage.updateFilm(film);
        return film;
    }
    //@GetMapping
    //public Film
}
