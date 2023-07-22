package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    protected DataController data = new DataController();
    @GetMapping
    public List<User> getUsers() {
        log.debug("Получен запрос GET /users.");
        return data.getListOfUsers();
    }
    @PostMapping
    public User createUser(@RequestBody User user) {
        log.debug("Получен запрос POST /users.");
        data.addUser(user);
        return user;
    }
    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        data.updateUser(user);
        return  user;
    }
}

