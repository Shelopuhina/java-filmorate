package ru.yandex.practicum.filmorate.daoImpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.DbFilmStorage;
import ru.yandex.practicum.filmorate.dao.DbUserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbFilmStorageTest {
    private final DbFilmStorage storage;
    private final DbUserStorage userStorage;

    @Test
    void getFilmByIdTest() {
        Film expectedFilm = Film.builder()
                .name("Film")
                .description("testFilm")
                .releaseDate(LocalDate.parse("2000-11-30"))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        storage.create(expectedFilm);

        Film filmById = storage.getFilmById(expectedFilm.getId());
        assertEquals(expectedFilm, filmById);
        storage.deleteFilm(expectedFilm.getId());

    }

    @Test
    void getFilmWithIncorrectId() {
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                () -> storage.getFilmById(100)
        );

        assertEquals("Фильм с id=100 не найден.", e.getMessage());
    }

    @Test
    void filmCreateTest() {
        Film expectedFilm = Film.builder()
                .name("Film")
                .description("testFilm")
                .releaseDate(LocalDate.parse("2000-11-30"))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        Film film = storage.create(expectedFilm);

        assertEquals(expectedFilm, film);
        storage.deleteFilm(expectedFilm.getId());
    }

    @Test
    void filmUpdateTest() {
        Film expectedFilm = Film.builder()
                .name("Film")
                .description("testFilm")
                .releaseDate(LocalDate.parse("2000-11-30"))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        storage.create(expectedFilm);
        Film updateFilm = Film.builder()
                .name("FilmUpdated")
                .description("UpdateTestFilm")
                .releaseDate(LocalDate.parse("2000-11-30"))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        updateFilm.setId(expectedFilm.getId());
        Film updatedFilm = storage.update(updateFilm);

        assertEquals(updateFilm, updatedFilm);
        storage.deleteFilm(expectedFilm.getId());
    }

    @Test
    void filmUpdateWithIncorrectIdTest() {
        Film expectedFilm = Film.builder()
                .name("Film")
                .description("testFilm")
                .releaseDate(LocalDate.parse("2000-11-30"))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    expectedFilm.setId(100);
                    storage.update(expectedFilm);
                }
        );

        assertEquals("Фильм с id=100 не найден.", e.getMessage());
        storage.deleteFilm(expectedFilm.getId());

    }


    @Test
    void getTopTenFilmsTest() {
        Film film1 = Film.builder()
                .name("Film")
                .description("testFilm")
                .releaseDate(LocalDate.parse("2000-11-30"))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        storage.create(film1);
        Film film2 = Film.builder()
                .name("Film2")
                .description("testFilm2")
                .releaseDate(LocalDate.parse("2002-11-30"))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();
        storage.create(film2);
        User user = User.builder()
                .email("user@email.ru")
                .login("userLogin")
                .name("name user")
                .birthday(LocalDate.parse("1999-11-30"))
                .build();
        userStorage.create(user);
        storage.addLike(film2.getId(), user.getId());
        List<Film> topTenFilms = storage.getTopTenFilms(10);

        assertEquals(film2, topTenFilms.get(0));
        assertEquals(film1, topTenFilms.get(1));
        storage.deleteFilm(film1.getId());
        storage.deleteFilm(film2.getId());
    }

    @Test
    void addAndDeleteGenresTest() {
        Film film = Film.builder()
                .name("Film")
                .description("testFilm")
                .releaseDate(LocalDate.parse("2000-11-30"))
                .duration(120)
                .mpa(new Mpa(1, "G"))
                .build();

        film.setGenres(List.of(new Genre(2, null)));
        storage.create(film);
        List<Genre> genres = storage.getFilmGenres(film.getId());

        assertEquals(List.of(new Genre(2, "Драма")), genres);

        storage.removeFilmGenres(film.getId());
        genres = storage.getFilmGenres(film.getId());
        assertTrue(genres.isEmpty());
        storage.deleteFilm(film.getId());

    }
}
