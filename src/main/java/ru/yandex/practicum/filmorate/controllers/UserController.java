package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userservice;

    @Autowired
    public UserController(UserService userservice) {
        this.userservice = userservice;
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable String id) {
        log.debug("Получен запрос GET /users/{id}.");
        return userservice.getUserById(Integer.parseInt(id));
    }


    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос DELETE /users/{id}/friends/{friendId}.");
        userservice.removeFriend(id, friendId);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос PUT /users/{id}/friends/{friendId}.");
        userservice.addFriend(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        log.debug("Получен запрос GET /users/{id}/friends.");
        return userservice.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getCommonFriendsList(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Получен запрос GET /users/{id}/friends/common/{otherId}.");
        return userservice.getCommonFriends(id, otherId);
    }

    @PostMapping("/users")
    public User createUser(@RequestBody User user) {
        log.debug("Получен запрос POST /users.");
        return userservice.addUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) {
        log.debug("Получен запрос PUT /users.");
        return userservice.updateUser(user);
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        log.debug("Получен запрос GET /users");
        return userservice.getUsers();
    }
}

