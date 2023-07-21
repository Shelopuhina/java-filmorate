package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private final int id;
    @NonNull
    private final String email;
    @NonNull
    private final String login;
    private String name;
    private final LocalDate birthday;
}
