package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Entity;

import java.util.List;

public interface Storage <T extends Entity> {
        T create(T data);

        T update(T data);

        T get(int id);

        List<T> getAll();
    }

