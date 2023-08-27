package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DbMpaStorage;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class DbMpaStorageImpl implements DbMpaStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Optional<Mpa> getMpaById(int mpaId) {
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("SELECT * FROM mpa WHERE mpa_id = ?",mpaId);
        if (sqlQuery.next()) {
            Mpa mpa = new Mpa(
                    sqlQuery.getInt("mpa_id"),
                    sqlQuery.getString("name"));
            return Optional.of(mpa);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbcTemplate.query("SELECT * FROM mpa",
                (resultSet, rowNum) -> buildMpa(resultSet));
    }

    public Mpa buildMpa(ResultSet rs) throws SQLException {
        return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
    }
}
