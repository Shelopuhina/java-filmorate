package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DbMpaStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MpaService {
    private final DbMpaStorage mpaStorage;
    public Mpa getMpa(int id) {
        return mpaStorage.getMpaById(id).orElseThrow(() -> {
            throw new NotFoundException("Мра с id" + id + " не существует.");
        });
    }
    public List<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }
}
