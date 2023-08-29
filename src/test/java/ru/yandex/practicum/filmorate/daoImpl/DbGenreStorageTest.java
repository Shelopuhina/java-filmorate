package ru.yandex.practicum.filmorate.daoImpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.DbGenreStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbGenreStorageTest {
    private final DbGenreStorage storage;

    @Test
    void getGenreByIdTest() {
        Genre genre = storage.getGenreById(1);
        assertEquals(new Genre(1, "Комедия"), genre);

        genre = storage.getGenreById(2);
        assertEquals(new Genre(2, "Драма"), genre);

        genre = storage.getGenreById(3);
        assertEquals(new Genre(3, "Мультфильм"), genre);

        genre = storage.getGenreById(4);
        assertEquals(new Genre(4, "Триллер"), genre);

        genre = storage.getGenreById(5);
        assertEquals(new Genre(5, "Документальный"), genre);

        genre = storage.getGenreById(6);
        assertEquals(new Genre(6, "Боевик"), genre);
    }

    @Test
    void getAllGenresTest() {
        List<Genre> genres = storage.getAllGenres();
        List<Genre> testList = List.of(
                new Genre(1, "Комедия"),
                new Genre(2, "Драма"),
                new Genre(3, "Мультфильм"),
                new Genre(4, "Триллер"),
                new Genre(5, "Документальный"),
                new Genre(6, "Боевик")
        );
        assertEquals(testList, genres);
    }


    @Test
    void getGenreWithIncorrectId() {
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                () -> storage.getGenreById(100)
        );

        assertEquals("Жанр с id=100 не найден.", e.getMessage());
    }


}