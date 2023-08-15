package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
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
    public Optional<Film> getFilmById(int filmId) {
        Film film = filmStorage.get(filmId);
        film.isValidate(film);
        return Optional.of(film);
    }

    @Override
    public void addLike(int userId, int filmId) {
        if (!getFilmById(filmId).isPresent())
            throw new NotFoundException("Фильм не найден.");
        Optional<Film> film = getFilmById(filmId);
        film.get().getLikes().add(userId);
    }

    @Override
    public void deleteLike(int userId, int filmId) {
        if (getFilmById(filmId).isPresent())
            throw new NotFoundException("Фильмне найден.");
        Optional<Film> film = getFilmById(filmId);
        film.get().getLikes().remove(userId);
    }

    @Override
    public List<Film> getTopTenFilms(int counts) {
        if (counts < 0) throw new NotFoundException("Топ должен содержать больше 1 элемента.");
        return getFilms().stream()
                .sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size())
                .limit(counts)
                .collect(Collectors.toList());
    }
}
