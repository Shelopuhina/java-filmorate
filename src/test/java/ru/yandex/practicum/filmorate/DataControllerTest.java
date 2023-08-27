package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DataControllerTest {

    @Test
    public void isValidFilmEmptyName() {
        Film film = new Film(1,
                " ",
                "American romantic fantasy film directed by Catherine Hardwicke",
                LocalDate.of(1794, 11, 21),
                121,
                new Mpa(1,"G")
        );

        var exc = assertThrows(ValidationException.class,
                () -> film.validate(film));
        assertEquals("Фильм невозможно добавить. Название фильма пустое.", exc.getMessage());
    }

    @Test
    public void isValidFilmLongDescription() {
        Film film = new Film(1,
                "twilight",
                ("Seventeen-year-old Bella Swan leaves Phoenix, Arizona and moves to Forks," +
                        "a small town located on Washington state's Olympic Peninsula, to live with her father, Charlie," +
                        "the town's police chief. Her mother, Renée, is remarried to Phil, a minor league baseball player " +
                        "whose career often keeps them on the road."),
                LocalDate.of(1794, 11, 21),
                121,
                new Mpa(1,"G")
        );



        var exc = assertThrows(ValidationException.class,
                () -> film.validate(film));
        assertEquals("Фильм невозможно добавить. Описание фильма больше 200 символов.", exc.getMessage());
    }

    @Test
    public void isValidFilmWrongReleaseDate() {
        Film film = new Film(1,
                "twilight",
                "American romantic fantasy film directed by Catherine Hardwicke",
                LocalDate.of(1794, 11, 21),
                121,
                new Mpa(1,"G")
                );

        var exc = assertThrows(ValidationException.class,
                () -> film.validate(film));
        assertEquals("Фильм невозможно добавить. Дата релиза фильма раньше 28.12.1895.", exc.getMessage());
    }

    @Test
    public void isValidFilmWrongDuration() {
        Film film = new Film(1,
                "twilight",
                "American romantic fantasy film directed by Catherine Hardwicke",
                LocalDate.of(2000, 11, 21),
                -120,
                new Mpa(1,"G")
        );

        var exc = assertThrows(ValidationException.class,
                () -> film.validate(film));
        assertEquals("Фильм невозможно добавить. Продолжительность не может быть отрицательной.", exc.getMessage());
    }

    @Test
    public void isValidUserWrongEmail() {
        User user = User.builder()
                .email("someonegmail.com")
                .login("Some4")
                .name("Alex")
                .birthday(LocalDate.of(1998, 10, 21))
                .build();
        var exc = assertThrows(ValidationException.class,
                () -> user.validate(user));
        assertEquals("Пользователя невозможно добавить. Email должен содержать - @.", exc.getMessage());
    }

    @Test
    public void isValidUserEmptyEmail() {
        User user = User.builder()
                .email(" ")
                .login("Some4")
                .name("Alex")
                .birthday(LocalDate.of(1998, 10, 21))
                .build();
        var exc = assertThrows(ValidationException.class,
                () -> user.validate(user));
        assertEquals("Пользователя невозможно добавить. Email не должен быть пустым.", exc.getMessage());
    }

    @Test
    public void isValidUserEmptyLogin() {
        User user = User.builder()
                .email("someone@gmail.com")
                .login(" ")
                .name("Alex")
                .birthday(LocalDate.of(1998, 10, 21))
                .build();
        var exc = assertThrows(ValidationException.class,
                () -> user.validate(user));
        assertEquals("Пользователя невозможно добавить. Login не должен быть пустым.", exc.getMessage());
    }

    @Test
    public void isValidUserWrongLogin() {
        User user = User.builder()
                .email("someone@gmail.com")
                .login("I am ")
                .name("Alex")
                .birthday(LocalDate.of(1998, 10, 21))
                .build();
        var exc = assertThrows(ValidationException.class,
                () -> user.validate(user));
        assertEquals("Пользователя невозможно добавить. Login не должен содержать пробелы.", exc.getMessage());
    }

    @Test
    public void isValidUserWrongName() {
        User user = User.builder()
                .email("someone@gmail.com")
                .login("Some4")
                .name("")
                .birthday(LocalDate.of(1998, 10, 21))
                .build();
        user.validate(user);
        assertEquals("Some4", user.getName());
    }

    @Test
    public void isValidUserWrongBirthday() {
        User user = User.builder()
                .email("someone@gmail.com")
                .login("Some4")
                .name("Alex")
                .birthday(LocalDate.of(2023, 10, 21))
                .build();
        var exc = assertThrows(ValidationException.class,
                () -> user.validate(user));
        assertEquals("Пользователя невозможно добавить. День рождения должен быть указан до " + LocalDate.now(), exc.getMessage());
    }
}
