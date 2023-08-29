package ru.yandex.practicum.filmorate.daoImpl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.DbUserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DbUserStorageTest {

    private final DbUserStorage storage;

    @Test
    void getUserByIdTest() {
        User expectedUser = User.builder()
                .email("user1@email.ru")
                .login("user1Login")
                .name("name1 user")
                .birthday(LocalDate.parse("1990-11-30"))
                .build();
        User addedUser = storage.create(expectedUser);

        User userById = storage.getUserById(addedUser.getId());
        assertEquals(expectedUser, userById);
        storage.deleteUser(expectedUser.getId());

    }

    @Test
    void getUserByIncorrectIdTest() {
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                () -> storage.getUserById(100)
        );

        assertEquals("Пользователь с id=100 не найден.", e.getMessage());
    }

    @Test
    void getAllUsersTest() {
        User user1 = User.builder()
                .email("user1@email.ru")
                .login("user1Login")
                .name("name1 user")
                .birthday(LocalDate.parse("1990-11-30"))
                .build();
        storage.create(user1);
        User user2 = User.builder()
                .email("user2@email.ru")
                .login("user2Login")
                .name("name2 user")
                .birthday(LocalDate.parse("1999-11-30"))
                .build();
        storage.create(user2);

        List<User> users = storage.getAllUsers();
        assertEquals(2, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
        storage.deleteUser(user1.getId());
        storage.deleteUser(user2.getId());
    }

    @Test
    void userCreateTest() {
        User expectedUser = User.builder()
                .email("user1@email.ru")
                .login("user1Login")
                .name("name1 user")
                .birthday(LocalDate.parse("1990-11-30"))
                .build();
        User user = storage.create(expectedUser);

        assertEquals(expectedUser, user);
        storage.deleteUser(user.getId());

    }

    @Test
    void userUpdateTest() {
        User expectedUser = User.builder()
                .email("user1@email.ru")
                .login("user1Login")
                .name("name1 user")
                .birthday(LocalDate.parse("1990-11-30"))
                .build();
        storage.create(expectedUser);
        User updateUser = User.builder()
                .email("user3@email.ru")
                .login("user3Login")
                .name("name33 user")
                .birthday(LocalDate.parse("1990-11-30"))
                .build();
        updateUser.setId(expectedUser.getId());
        User updatedUser = storage.update(updateUser);

        assertEquals(updateUser, updatedUser);
        storage.deleteUser(updatedUser.getId());
    }

    @Test
    void userUpdateWithIncorrectId() {
        NotFoundException e = Assertions.assertThrows(
                NotFoundException.class,
                () -> {
                    User expectedUser = User.builder()
                            .id(100)
                            .email("user1@email.ru")
                            .login("user1Login")
                            .name("name1 user")
                            .birthday(LocalDate.parse("1990-11-30"))
                            .build();
                    storage.update(expectedUser);
                }
        );

        assertEquals("Пользователь с id=100 не найден.", e.getMessage());

    }
}