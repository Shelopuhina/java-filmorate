package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DbMpaStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.DbFilmStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FilmService {
    private final DbFilmStorage filmDb;
    private final DbMpaStorage mpaStorage;
    // private int count;

    public void addLike(int filmId, int userId) {
        if (getFilmById(filmId) == null)
            throw new NotFoundException("Фильм не найден.");
        if (userId < 0) throw new NotFoundException("id не может быть отрицательным");
        Film film = getFilmById(filmId);
        film.validate(film);
        filmDb.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        if (getFilmById(filmId) == null)
            throw new NotFoundException("Фильмне найден.");
        if (userId < 0) throw new NotFoundException("id не может быть отрицательным");
        Film film = getFilmById(filmId);
        film.validate(film);
        filmDb.deleteLike(filmId, userId);
    }

    public List<Film> getTopTenFilms(int counts) {
        if (counts < 0) throw new NotFoundException("Топ должен содержать больше 1 элемента.");
        List<Film> films = filmDb.getTopTenFilms(counts);
        for (Film film : films) {
            film.setGenres(filmDb.getFilmGenres(film.getId()));
        }
        return films;
    }

    public Film addFilm(Film film) {
        if (film == null) throw new NotFoundException("Невозможно сохранить пустой объект.");
        film.validate(film);
        Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId());
        film.setMpa(mpa);
        return filmDb.addFilmGenres(filmDb.create(film));
    }

    public Film updateFilm(Film film) {
        if (film == null) throw new NotFoundException("Невозможно обновить пустой объект.");
        film.validate(film);
        filmDb.removeFilmGenres(film.getId());
        Film filmUpdated = filmDb.update(film);
        filmDb.addFilmGenres(filmUpdated);
        return film;
    }

    public Film getFilmById(int id) {
        Film film = filmDb.getFilmById(id);
        film.setGenres(filmDb.getFilmGenres(film.getId()));
        return film;
    }


    public List<Film> getFilms() {
        List<Film> films = filmDb.getAllFilms();
        films.forEach(film -> film.setGenres(filmDb.getFilmGenres(film.getId())));
        return films;
    }
}


