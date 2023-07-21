package ru.yandex.practicum.filmorate.controllers;

import java.util.List;

public interface Handler {
    Object add(Object obj);

    Object update(Object obj);

    List<Object> getList();
}
