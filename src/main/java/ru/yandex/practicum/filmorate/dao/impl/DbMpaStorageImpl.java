package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DbMpaStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DbMpaStorageImpl implements DbMpaStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Mpa getMpaById(int mpaId) {
        try {
            String sqlQuery = "SELECT * FROM mpa WHERE mpa_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::buildMpa, mpaId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Mpa с id=" + mpaId + " не найден.");
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa",
                this::buildMpa);
    }

    public Mpa buildMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
    }
}
