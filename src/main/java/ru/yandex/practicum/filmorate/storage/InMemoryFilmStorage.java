package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer, Film> filmStorage = new HashMap<>();
    private int count = 1;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public Film addFilm(Film film) {
        if (film == null) throw new NotExpectedException("Невозможно сохранить пустой объект.");
        film.isValidate(film);
        if (filmStorage.containsKey(film.getId()))
            throw new NotExpectedException("Объект с id " +
                    film.getId() + " уже зарегистрирован.");
        film.setId(count);
        count++;
        filmStorage.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (filmStorage.containsKey(film.getId())) {
            film.isValidate(film);
            filmStorage.put(film.getId(), film);
            log.debug("Объект с id " + film.getId() + " обновлен.");
        } else {
            throw new NotExpectedException("Сначала добавьте объект в систему.");
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<Film>(filmStorage.values());
    }

    @Override
    public void deleteFilm(Film film) {
        if (film == null) throw new NotExpectedException("Невозможно удалить пустой объект.");
        if (filmStorage.containsKey(film.getId())) {
            filmStorage.remove(film.getId());
        }else{
            throw new NotExpectedException("Сначала добавьте объект в систему.");
        }
    }

    @Override
    public Optional<Film> getFilmById(int filmId) {
        return filmStorage.values().stream()
                .filter(x -> x.getId() == filmId)
                .findFirst();
    }
}
