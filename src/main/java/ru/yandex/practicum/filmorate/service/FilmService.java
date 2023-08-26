package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DbFilmStorage;

import java.util.ArrayList;
import java.util.List;


@Service
public class FilmService {
    private final DbFilmStorage filmDb;

    public FilmService(DbFilmStorage filmDb) {
        this.filmDb = filmDb;
    }

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
        return filmDb.getTopTenFilms(counts);
    }

    public Film addFilm(Film film) {
        if(film == null)  throw new NotFoundException("Невозможно сохранить пустой объект.");
        film.validate(film);
        var mpa = mpaRepository.getMpaById(film.getMpa().getId())
                .orElseThrow(() -> {
                    throw new NotFoundException("MPA doesn't exist");
                });
        film.setId(generateId());
        addFilmGenres(film);
        film.setMpa(mpa);
        return filmDb.create(film);
    }

    public Film updateFilm(Film film) {
        return filmDb.update(film);
    }

    public Film getFilmById(int id) {
        if(filmDb.getFilmById(id).isPresent()) {
            return filmDb.getFilmById(id).get();
        }else{
            throw new NotFoundException("Фильм не существует.");
        }
    }

    public List<Film> getFilms() {
        return new ArrayList<>(filmDb.getAllFilms());
    }
}


