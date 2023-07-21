package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Data
public class Film {
    private int id;
    @NonNull
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final Duration duration;
    private List<Film> films;
    }
