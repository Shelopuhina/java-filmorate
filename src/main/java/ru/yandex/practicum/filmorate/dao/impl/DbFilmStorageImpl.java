package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DbFilmStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            addFilmGenres(film);
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

@Override
    public Film addFilmGenres(Film film) {
        if(film.getGenres() !=null) {
            List<Genre> genres = film.getGenres()
                    .stream()
                    .distinct()
                    .collect(Collectors.toList());
            film.setGenres(genres);

            String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) VALUES (?,?)";
            genres.forEach(genre -> jdbcTemplate.update(sqlQuery, film.getId(), genre.getId()));
        }
            return film;
    }
@Override
    public void removeFilmGenres(int filmId) {
        jdbcTemplate.update("DELETE FROM film_genre WHERE film_id = ?", filmId);
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?,  mpa_id = ? " +
                "WHERE film_id = ?";
        int rowsUpdated =jdbcTemplate.update(sqlQuery
                , film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        if (rowsUpdated == 1) {
            log.info("Обновлен фильм {}.", film);
            return film;
        } else {
            int filmId = film.getId();
            throw new NotFoundException("Фильм с id=" + filmId + " не найден.");
        }
    }
    @Override
    public List<Genre> getFilmGenres(int filmId) {
        return jdbcTemplate.query(
                "SELECT * FROM genre INNER JOIN film_genre ON film_genre.genre_id = genre.genre_id " +
                        "AND film_genre.film_id = ?",
                this::buildFilmGenre, filmId);
    }

    public Genre buildFilmGenre(ResultSet rs,int rowNum) throws SQLException {
        return new Genre(rs.getInt("genre_id"), rs.getString("name"));
    }

    @Override
    public Film getFilmById(int id)  {
        try{
            String sqlQuery = "SELECT f.*, m.name AS mpa_name FROM films AS f "+
                    "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id AND f.film_id = ?";
            return jdbcTemplate.queryForObject(sqlQuery,this::filmBuilder, id);
        }catch (EmptyResultDataAccessException e) {
        throw new NotFoundException("Фильм с id=" + id + " не найден.");
    }
    }

    @Override
    public List<Film> getAllFilms() {
        return jdbcTemplate.query(
                "SELECT f.*, m.name AS mpa_name FROM films AS f INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id",
                 this::filmBuilder);
    }

    public Film filmBuilder(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(resultSet.getInt("film_id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("MPA_NAME")))
                .build();
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
         return jdbcTemplate.query(
                "SELECT f.*, m.mpa_id, m.name AS mpa_name " +
                        "FROM films AS f " +
                        "INNER JOIN mpa AS m ON f.mpa_id = m.mpa_id " +//check sql
                        "LEFT OUTER JOIN film_likes AS fl ON f.film_id = fl.film_id " +
                        "GROUP BY f.film_id, fl.user_id " +
                        "ORDER BY COUNT(fl.user_id) DESC " +
                        "LIMIT ?", this::filmBuilder, counts);


    }

}










