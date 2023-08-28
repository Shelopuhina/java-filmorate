package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;
@AllArgsConstructor
@RestController
@Slf4j
public class FilmController {
    private final FilmService filmService;
    private final GenreService genreService;
    private final MpaService mpaService;


    @PostMapping("/films")
    public Film createFilm(@RequestBody Film film) {
        log.debug("Получен запрос POST /films." + film.toString());
        filmService.addFilm(film);
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
            log.debug("Получен запрос PUT /films.");
        filmService.updateFilm(film);
        return film;
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTopFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getTopTenFilms(count);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.debug("Выполнен GET-запрос /films");
        return filmService.getFilms();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        log.debug("Выполнен GET-запрос /genres/{id}");
        return genreService.getGenre(id);
    }

    @GetMapping("/genres")
    public List<Genre> getGenres() {
        return genreService.getAllGenres();
    }
    @GetMapping("/mpa/{id}")
    public Mpa getMpa(@PathVariable int id) {
        log.debug("Выполнен GET-запрос /mpa/{id}");
        return mpaService.getMpa(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getAll() {
        return mpaService.getAllMpa();
    }
}

