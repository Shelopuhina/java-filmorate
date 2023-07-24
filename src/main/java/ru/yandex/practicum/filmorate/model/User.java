package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exceptions.NotExpectedException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class User extends Entity {
    @NonNull
    private final String email;
    @NonNull
    private final String login;
    private String name;
    private final LocalDate birthday;

    @Override
    public void isValidate(Object obj) {
        if (obj.getClass().equals(User.class)) {
            User user = (User) obj;
            if (user.getEmail().isBlank())
                throw new ValidationException("Пользователя невозможно добавить. Email не должен быть пустым.");
            if (!user.getEmail().contains("@"))
                throw new ValidationException("Пользователя невозможно добавить. Email должен содержать - @.");
            if (user.getLogin().isBlank())
                throw new ValidationException("Пользователя невозможно добавить. Login не должен быть пустым.");
            if (user.getLogin().contains(" "))
                throw new ValidationException("Пользователя невозможно добавить. Login не должен содержать пробелы.");
            if (user.getName() == null)
                user.setName(user.getLogin());
            if (user.getBirthday().isAfter(LocalDate.now()))
                throw new ValidationException("Пользователя невозможно добавить. День рождения должен быть указан до " + LocalDate.now());//проеврить вывод при эксепшене

        } else {
            throw new NotExpectedException("Попытка обновить неизвестный объект.");
        }
    }
}
