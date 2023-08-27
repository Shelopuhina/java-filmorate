package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DbGenreStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService {
    private final DbGenreStorage genreStorage;
    public Genre getGenre(int id) {
        return genreStorage.getGenreById(id).orElseThrow(() -> {
            throw new NotFoundException("Жанр с id "+id+"не добавлен в систему.");
        });
    }
    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}
