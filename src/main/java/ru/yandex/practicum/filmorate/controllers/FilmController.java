package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    protected DataController<Film> data = new DataController<>();


    @GetMapping
    public List<Film> getFilms() {
        log.debug("Получен запрос GET /films.");
        return data.getList();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.debug("Получен запрос POST /films." + film.toString());
        data.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.debug("Получен запрос PUT /films.");
        data.update(film);
        return film;
    }
}
