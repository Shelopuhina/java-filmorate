package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.DbUserStorage;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Component
public class DbUserStorageImpl implements DbUserStorage {
    private final JdbcTemplate jdbcTemplate;

    public DbUserStorageImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public User create(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
        int id  = simpleJdbcInsert.executeAndReturnKey(userToRow(user)).intValue();
        user.setId(id);
        return user;
    }

    public static Map<String, Object> userToRow(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());
        return values;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE user_id = ?";
        int userId = user.getId();
        int rowsUpdated = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                userId
        );
        if (rowsUpdated == 1) {
            log.info("Обновлен пользователь {}.", user);
            return user;
        } else {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден.");
        }
    }

    @Override
    public User getUserById(int id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = ?", this::buildUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с id=" + id + " не найден.");
        }
    }


    @Override
    public List<User> getAllUsers() {
        return jdbcTemplate.query(
                "SELECT * FROM users", this::buildUser);
    }

    public User buildUser(ResultSet resultSet, int rowNum) throws SQLException {
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
    public void addFriend(int userId, int friendId) {
        jdbcTemplate.update("INSERT INTO friend_list (user_id,friend_Id) VALUES(?,?)", userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update("DELETE FROM friend_list WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }

    @Override
    public void deleteUser(int userId) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ? ", userId);
    }

    @Override
    public List<Integer> getFriends(int userId) {
        return jdbcTemplate.query(
                "SELECT friend_id FROM friend_list WHERE user_id = ?",
                (resultSet, rowNum) -> resultSet.getInt("friend_id"), userId);
    }


}










