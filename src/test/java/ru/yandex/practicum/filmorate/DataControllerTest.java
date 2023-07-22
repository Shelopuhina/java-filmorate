package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controllers.DataController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DataControllerTest {
    DataController data = new DataController();
    @Test
    public void isValidFilmEmptyName() {
        Film film = Film.builder()
                .name(" ")
                .description("American romantic fantasy film directed by Catherine Hardwicke")
                .releaseDate(LocalDate.of(2008,11,21))
                .duration(121)
                .build();

        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(film));
               assertEquals("Фильм невозможно добавить. Название фильма пустое.",exc.getMessage());
    }
    @Test
    public void isValidFilmLongDescription() {
        Film film = Film.builder()
                .name("twilight")
                .description("Seventeen-year-old Bella Swan leaves Phoenix, Arizona and moves to Forks,"+
                        "a small town located on Washington state's Olympic Peninsula, to live with her father, Charlie,"+
                        "the town's police chief. Her mother, Renée, is remarried to Phil, a minor league baseball player "+
                        "whose career often keeps them on the road.")
                .releaseDate(LocalDate.of(2008,11,21))
                .duration(121)
                .build();

        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(film));
        assertEquals("Фильм невозможно добавить. Описание фильма больше 200 символов.",exc.getMessage());
    }

    @Test
    public void isValidFilmWrongReleaseDate() {
        Film film = Film.builder()
                .name("twilight")
                .description("American romantic fantasy film directed by Catherine Hardwicke")
                .releaseDate(LocalDate.of(1994,11,21))
                .duration(121)
                .build();

        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(film));
        assertEquals("Фильм невозможно добавить. Дата релиза фильма раньше 28.12.1995.",exc.getMessage());
    }
    @Test
    public void isValidFilmWrongDuration() {
        Film film = Film.builder()
                .name("twilight")
                .description("American romantic fantasy film directed by Catherine Hardwicke")
                .releaseDate(LocalDate.of(2008,11,21))
                .duration(-121)
                .build();

        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(film));
        assertEquals("Фильм невозможно добавить. Продолжительность не может быть отрицательной.",exc.getMessage());
    }

    @Test
    public void isValidUserWrongEmail() {
        User user = User.builder()
                .email("someonegmail.com")
                .login("Some4")
                .name("Alex")
                .birthday(LocalDate.of(1998,10,21))
                .build();
        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(user));
        assertEquals("Пользователя невозможно добавить. Email должен содержать - @.",exc.getMessage());
    }
    @Test
    public void isValidUserEmptyEmail() {
        User user = User.builder()
                .email(" ")
                .login("Some4")
                .name("Alex")
                .birthday(LocalDate.of(1998,10,21))
                .build();
        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(user));
        assertEquals("Пользователя невозможно добавить. Email не должен быть пустым.",exc.getMessage());
    }
    @Test
    public void isValidUserEmptyLogin() {
        User user = User.builder()
                .email("someone@gmail.com")
                .login(" ")
                .name("Alex")
                .birthday(LocalDate.of(1998,10,21))
                .build();
        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(user));
        assertEquals("Пользователя невозможно добавить. Login не должен быть пустым.",exc.getMessage());
    }
    @Test
    public void isValidUserWrongLogin() {
        User user = User.builder()
                .email("someone@gmail.com")
                .login("I am ")
                .name("Alex")
                .birthday(LocalDate.of(1998,10,21))
                .build();
        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(user));
        assertEquals("Пользователя невозможно добавить. Login не должен содержать пробелы.",exc.getMessage());
    }
    @Test
    public void isValidUserWrongName() {
        User user = User.builder()
                .email("someone@gmail.com")
                .login("Some4")
                .name(null)
                .birthday(LocalDate.of(1998,10,21))
                .build();
        data.isValidate(user);
                assertEquals("Some4",user.getName());
    }
    @Test
    public void isValidUserWrongBirthday() {
        User user = User.builder()
                .email("someone@gmail.com")
                .login("Some4")
                .name("Alex")
                .birthday(LocalDate.of(2023,10,21))
                .build();
        var exc = assertThrows(ValidationException.class,
                ()-> data.isValidate(user));
        assertEquals("Пользователя невозможно добавить. День рождения должен быть указан до " + LocalDate.now(),exc.getMessage());
    }
}
