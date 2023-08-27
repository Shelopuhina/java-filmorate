package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DbUserStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DbUserStorageImpl implements DbUserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbUserStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User create(User user) {
        String sqlQuery = "INSERT INTO users(user_id, email, login, name, birthday)" +
                "VALUES(?,?,?,?,?)";

        jdbcTemplate.update(sqlQuery,
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()));
        return user;
    }



    @Override
    public User update(User user) {
        jdbcTemplate.update(
                "DELETE FROM users WHERE user_id = ?",
                user.getId());

        jdbcTemplate.update(
                "INSERT INTO users (user_id, email, login, name, birthday) " +
                        "VALUES (?,?,?,?,?)",
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        SqlRowSet sqlQuery =  jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?", id);

        if (sqlQuery.next()) {
            var user = User.builder()
                    .id(sqlQuery.getInt("user_id"))
                    .email(Objects.requireNonNull(sqlQuery.getString("email")))
                    .login(Objects.requireNonNull(sqlQuery.getString("login")))
                    .name(sqlQuery.getString("name"))
                    .birthday(Objects.requireNonNull(sqlQuery.getDate("birthday")).toLocalDate())
                    .build();
            user.getFriends().addAll(getFriends(id));
            return Optional.of(user);
        } else {
            return Optional.empty();
        }
    }


    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM users",
                (resultSet, rowNum) -> buildUser(resultSet));
    }

    public User buildUser(ResultSet resultSet) throws SQLException {
        User user = User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        user.getFriends().addAll(getFriends(user.getId()));
        return user;
    }
    @Override
    public void addFriend(int userId,int friendId) {
        jdbcTemplate.update("INSERT INTO friend_list (user_id,friend_Id) VALUES(?,?)", userId,friendId);
    }
    @Override
    public void deleteFriend(int userId,int friendId) {
        jdbcTemplate.update("DELETE FROM friend_list WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }
    @Override
    public List<Integer> getFriends(int userId) {
       return   jdbcTemplate.query(
                "SELECT friend_id FROM friend_list WHERE user_id = ?",
                (resultSet, rowNum) -> resultSet.getInt("friend_id"), userId);
    }


}










