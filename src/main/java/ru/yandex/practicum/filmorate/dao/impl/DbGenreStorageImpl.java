package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DbGenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class DbGenreStorageImpl implements DbGenreStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Optional<Genre> getGenreById(int id) {
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("SELECT * FROM genre WHERE genre_id = ?",id);
        if (sqlQuery.next()) {
            Genre genre = new Genre(
                    sqlQuery.getInt("genre_id"),
                    sqlQuery.getString("name"));
            return Optional.of(genre);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM genre",
                (resultSet, rowNum) -> buildGenre(resultSet));
    }

    public Genre buildGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }

}

