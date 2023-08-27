package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DbFilmStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DbFilmStorageImpl implements DbFilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbFilmStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");
        film.setId(simpleJdbcInsert.executeAndReturnKey(filmToMap(film)).intValue());

        if (film.getGenres() != null) {
            List<Genre> genres = film.getGenres()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());

            addFilmGenres(film.getId(), genres);

        }
        return film;
    }
    public static Map<String, Object> filmToMap(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("mpa_id", film.getMpa().getId());
        return values;
    }

    public void addFilmGenres(int filmId, List<Genre> genres) {
        String sqlQuery =   "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";
            genres.forEach(genre -> jdbcTemplate.update(sqlQuery, filmId, genre.getId()));

    }

    public void removeFilmGenres(int filmId, List<Integer> genres) {
        jdbcTemplate.batchUpdate(
                "DELETE FROM film_genre WHERE film_id = ? AND genre_id = ?",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int value)
                            throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genres.get(value));
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?,  mpa_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery
                , film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        List<Genre> filmGenres = getFilmGenres(film.getId());

        List<Genre> addGenre = film.getGenres().stream()
                .filter(genre -> !filmGenres.contains(genre))
                .distinct()
                .collect(Collectors.toList());
        film.getGenres().clear();
        addFilmGenres(film.getId(), addGenre);
        film.getGenres().addAll(addGenre);
        return film;
    }
    @Override
    public List<Genre> getFilmGenres(int filmId) {
        return jdbcTemplate.query(
                "SELECT * FROM genre INNER JOIN film_genre ON film_genre.genre_id = genre.genre_id " +
                        "AND film_genre.film_id = ?",
                (resultSet, rowNum) -> buildFilmGenre(resultSet), filmId);
    }

    public Genre buildFilmGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }

    @Override
    public Optional<Film> getFilmById(int id) {
        SqlRowSet sqlQuery = jdbcTemplate.queryForRowSet("SELECT f.*, m.name AS mpa_name FROM films AS f "  +
                                  "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id AND f.film_id = ?",id);
        if (sqlQuery.next()) {
            var film = Film.builder()
                    .id(sqlQuery.getInt("film_id"))
                    .name(Objects.requireNonNull(sqlQuery.getString("name")))
                    .description(sqlQuery.getString("description"))
                    .releaseDate(Objects.requireNonNull(sqlQuery.getDate("release_date")).toLocalDate())
                    .duration(sqlQuery.getInt("duration"))
                    .mpa(new Mpa(sqlQuery.getInt("mpa_id"), sqlQuery.getString("MPA_NAME")))
                    .build();
            film.getGenres().addAll(getFilmGenres(id));
            film.getLikes().addAll(getUserLikes(id));
            return Optional.of(film);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(
                "SELECT f.*, m.name AS mpa_name FROM films AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id",
                (resultSet, rowNum) -> filmBuilder(resultSet));
    }

    public Film filmBuilder(ResultSet resultSet) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("MPA_NAME")))
                .build();
        film.getGenres().addAll(getFilmGenres(film.getId()));
        film.getLikes().addAll(getUserLikes(film.getId()));
        return film;
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbcTemplate.update("INSERT INTO film_likes(film_id, user_id) " +
                "VALUES(?,?)", filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM film_likes WHERE user_id = ? AND film_id = ?", userId, filmId);
    }

    public List<Integer> getUserLikes(int filmId) {
        return jdbcTemplate.query(
                "SELECT user_id FROM film_likes WHERE film_id = ?", (resultSet, rowNum) ->
                        resultSet.getInt("user_id"), filmId);
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
        return films;
    }

}










