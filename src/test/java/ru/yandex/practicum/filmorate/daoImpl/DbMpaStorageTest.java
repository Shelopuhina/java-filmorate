package ru.yandex.practicum.filmorate.daoImpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.DbMpaStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbMpaStorageTest {
    private final DbMpaStorage storage;

    @Test
    void getMpaByIdTest() {
        Mpa rate = storage.getMpaById(1);
        assertEquals(new Mpa(1, "G"), rate);

        rate = storage.getMpaById(2);
        assertEquals(new Mpa(2, "PG"), rate);

        rate = storage.getMpaById(3);
        assertEquals(new Mpa(3, "PG-13"), rate);

        rate = storage.getMpaById(4);
        assertEquals(new Mpa(4, "R"), rate);

        rate = storage.getMpaById(5);
        assertEquals(new Mpa(5, "NC-17"), rate);

    }

    @Test
    void getAllMpaTest() {
        List<Mpa> rates = storage.getAllMpa();
        List<Mpa> testList = List.of(
                new Mpa(1, "G"),
                new Mpa(2, "PG"),
                new Mpa(3, "PG-13"),
                new Mpa(4, "R"),
                new Mpa(5, "NC-17")
        );
        assertEquals(testList, rates);
    }


    @Test
    void getMpaWithIncorrectId() {
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                () -> storage.getMpaById(100)
        );

        assertEquals("Mpa с id=100 не найден.", e.getMessage());
    }
}
