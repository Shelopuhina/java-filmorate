package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface DbMpaStorage {
    Mpa getMpaById(int id);

    List<Mpa> getAllMpa();
}
