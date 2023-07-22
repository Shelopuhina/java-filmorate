package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NonNull
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
}
