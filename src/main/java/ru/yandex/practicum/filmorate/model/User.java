package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@Data
@Builder
public class User {
    private int id;
    @NonNull
    private final String email;
    @NonNull
    private final String login;
    private String name;
    private final LocalDate birthday;
    private final String friendStatus;
    private final Set<Integer> friends = new HashSet<>();


    public void validate(User user) {
            if (user.getEmail().isBlank())
                throw new ValidationException("Пользователя невозможно добавить. Email не должен быть пустым.");
            if (!user.getEmail().contains("@"))
                throw new ValidationException("Пользователя невозможно добавить. Email должен содержать - @.");
            if (user.getLogin().isBlank())
                throw new ValidationException("Пользователя невозможно добавить. Login не должен быть пустым.");
            if (user.getLogin().contains(" "))
                throw new ValidationException("Пользователя невозможно добавить. Login не должен содержать пробелы.");
            if (user.getName().isBlank())
                user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now()))
                throw new ValidationException("Пользователя невозможно добавить. День рождения должен быть указан до " + LocalDate.now());//проеврить вывод при эксепшене

    }
}
