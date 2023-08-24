package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Builder
@Data
@AllArgsConstructor
public class Film extends Entity {
    private int id;
    @NonNull
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    @NonNull
    private Mpa mpa;

    private final Set<Integer> likes = new HashSet<>();

    private final List<Genre> genres = new ArrayList<>();

    @Override
    public void validate(Entity entity) {
        if (entity.getClass().equals(Film.class)) {
            Film film = (Film) entity;
            if (film.getName().isBlank())
                throw new ValidationException("Фильм невозможно добавить. Название фильма пустое.");
            if (film.getDescription().length() > 200)
                throw new ValidationException("Фильм невозможно добавить. Описание фильма больше 200 символов.");
            if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
                throw new ValidationException("Фильм невозможно добавить. Дата релиза фильма раньше 28.12.1895.");
            if (film.getDuration() < 0)
                throw new ValidationException("Фильм невозможно добавить. Продолжительность не может быть отрицательной.");
        } else {
            throw new NotFoundException("Попытка обновить неизвестный объект.");
        }
    }
}

