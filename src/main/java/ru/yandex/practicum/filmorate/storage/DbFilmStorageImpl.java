package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DbFilmStorageImpl implements DbFilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private int count = 1;

    @Override
    public Film create(Film film) {
        String sqlQuery = "INSERT INTO films(film_id, name, description, release_date, duration, mpa_id)" +
                "VALUES(?,?,?,?,?,?)";

        jdbcTemplate.update(sqlQuery,
                film.getId(),
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getMpaId());

        if (film.getGenres() != null) {
            var genreId = film.getGenres()
                    .stream()
                    .map(Genre::getGenreId)
                    .collect(Collectors.toList());
            //genre!!!!!!!!!!!!!!!!!
        }
        return film;
    }

    public void addFilmGenres(int filmId, List<Integer> genres) {
        // jdbcTemplate.batchUpdate(
        //         "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)",

       /* final String INSERT_SQL = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] { "id" });
            ps.setString(1, name);
            return ps;
        }, keyHolder);





                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement preparedStatement, int value)
                            throws SQLException {
                        preparedStatement.setInt(1, filmId);
                        preparedStatement.setInt(2, genres.get(value));
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });*/
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?,  mpa_id = ?" +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getMpaId(),
                film.getId());
        return film;
    }

    @Override
    public Film get(int id) {
        String sqlQuery = "SELECT film_id, name, description, release_date, duration, mpa_id" +
                "FROM films WHERE film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, Film.class, id);
    }


    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(
                "SELECT f.*, m.name AS mpa_name, m.mpa_id FROM film AS f INNER JOIN mpa AS m ON f.mpa = m.id",
                (resultSet, rowNum) -> filmBuilder(resultSet));
    }

    public Film filmBuilder(ResultSet resultSet) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("name")))
                .build();
        return film;
        //genres,likes!!!!!!!
    }
    @Override
    public void addLike(int filmId, int userId) {
        jdbcTemplate.update("INSERT INTO film_likes(film_id, user_id)" +
                "VALUES(?,?)", filmId, userId);
    }
    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM film_likes WHERE user_id = ? AND film_id = ?", userId, filmId);
    }
    @Override
    public List<Film> getTopTenFilms(int counts) {
        var films = jdbcTemplate.query(
                "SELECT f.*, m.mpa_id, m.name AS mpa_name " +
                        "FROM films AS f " +
                        "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +//check sql
                        "LEFT OUTER JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                        "GROUP BY f.film_id, fl.user_id " +
                        "ORDER BY COUNT(fl.user_id) DESC " +
                        "LIMIT ?", (resultSet, rowNum) -> filmBuilder(resultSet), counts);

        //films.forEach(film -> {

        // });genre!!!!likes!!!
        return films;
    }

}










