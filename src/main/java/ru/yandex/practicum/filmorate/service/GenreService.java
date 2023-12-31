package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.DbGenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GenreService {
    private final DbGenreStorage genreStorage;

    public Genre getGenre(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }
}
