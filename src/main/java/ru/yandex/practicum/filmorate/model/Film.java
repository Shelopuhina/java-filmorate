package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class Film extends Entity {
    @NonNull
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private final Set<Integer> likes = new HashSet<>();

    @Override
    public void isValidate(Object obj) {
        if (obj.getClass().equals(Film.class)) {
            Film film = (Film) obj;
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
