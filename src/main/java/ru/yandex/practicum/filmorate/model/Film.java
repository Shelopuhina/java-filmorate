package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Builder
public class Film {
    private int id;
    private final String name;
    @NonNull
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    @NonNull
    private Mpa mpa;

    private Set<Integer> likes = new HashSet<>();

    private List<Genre> genres = new ArrayList<>();

    public void validate(Film film) {

        if (film.getName().isBlank())
            throw new ValidationException("Фильм невозможно добавить. Название фильма пустое.");
        if (film.getDescription().length() > 200)
            throw new ValidationException("Фильм невозможно добавить. Описание фильма больше 200 символов.");
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)))
            throw new ValidationException("Фильм невозможно добавить. Дата релиза фильма раньше 28.12.1895.");
        if (film.getDuration() < 0)
            throw new ValidationException("Фильм невозможно добавить. Продолжительность не может быть отрицательной.");
    }
}

