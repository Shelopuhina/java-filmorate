package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final HashMap<Integer, Film> filmStorage = new HashMap<>();
    private int count = 1;

    @Override
    public Film addFilm(Film film) {
        if (film == null) throw new NotFoundException("Невозможно сохранить пустой объект.");
        film.isValidate(film);
        if (filmStorage.containsKey(film.getId()))
            throw new NotFoundException("Объект с id " +
                    film.getId() + " уже зарегистрирован.");
        filmStorage.put(count, film);
        film.setId(count);
        count++;
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmStorage.containsKey(film.getId())) {
            film.isValidate(film);
            filmStorage.put(film.getId(), film);
            log.debug("Объект с id " + film.getId() + " обновлен.");
        } else {
            throw new NotFoundException("Сначала добавьте объект в систему.");
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<Film>(filmStorage.values());
    }

    @Override
    public void deleteFilm(Film film) {
        if (film == null) throw new NotFoundException("Невозможно удалить пустой объект.");
        if (filmStorage.containsKey(film.getId())) {
            filmStorage.remove(film.getId());
        } else {
            throw new NotFoundException("Сначала добавьте объект в систему.");
        }
    }

    @Override
    public Film getFilmById(int filmId) {
        if (filmStorage.get(filmId) == null) {
            throw new ValidationException("Фильм с id " + filmId + "не найден.");
        }
        Film film = filmStorage.get(filmId);
        film.isValidate(film);
        return film;
    }

    @Override
    public void addLike(int userId, int filmId) {
        if (getFilmById(filmId) == null)
            throw new NotFoundException("Фильм не найден.");
        if (userId < 0) throw new NotFoundException("id не может быть отрицательным");
        Film film = getFilmById(filmId);
        film.getLikes().add(userId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        if (getFilmById(filmId) == null)
            throw new NotFoundException("Фильмне найден.");
        if (userId < 0) throw new NotFoundException("id не может быть отрицательным");
        Film film = getFilmById(filmId);
        film.getLikes().remove(userId);
    }

    @Override
    public List<Film> getTopTenFilms(int counts) {
        if (counts < 0) throw new NotFoundException("Топ должен содержать больше 1 элемента.");
        List<Film> list = getFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(counts)
                .collect(Collectors.toList());
        return list;
    }
}
