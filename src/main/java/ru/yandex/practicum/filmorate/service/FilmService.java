package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DbMpaStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.dao.DbFilmStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
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
        return filmDb.getTopTenFilms(counts);
    }

    public Film addFilm(Film film) {
        if(film == null)  throw new NotFoundException("Невозможно сохранить пустой объект.");
        film.validate(film);
        //film.setId(generateId());
        Mpa mpa = mpaStorage.getMpaById(film.getMpa().getId()).orElseThrow(() -> {
                    throw new NotFoundException("Mpa не найден в базе данных.");
                });
        film.setMpa(mpa);

      /* if (film.getGenres().size() != filmDb.getFilmGenres(film.getId()).size()) {

        }
            var genresIds = film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .distinct()
                    .collect(Collectors.toList());
            var genres = filmRepository.getGenresByIds(genresIds);
            if (genres.size() != genresIds.size()) {
                throw new NotFoundException("Genre doesn't exist");
            }
            film.getGenres().clear();
            film.getGenres().addAll(genres.values());
        Film filmBefUpd = getFilmById(film.getId());
        List<Genre> filmBefUpdGenres = filmBefUpd.getGenres();
        List<Genre> filmGenres = film.getGenres();
        filmBefUpdGenres.clear();
        filmBefUpdGenres.addAll(filmGenres);*/

        return filmDb.create(film);
    }

    public Film updateFilm(Film film) {
        if (film == null) throw new NotFoundException("Невозможно обновить пустой объект.");
        try {
            film.validate(film);
            getFilmById(film.getId());
            return filmDb.update(film);
        }catch (NotFoundException exc) {
            throw new NotFoundException("Фильм не найден");
        }
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
   // public int generateId(){
     //     return ++count;
  //  }
}


