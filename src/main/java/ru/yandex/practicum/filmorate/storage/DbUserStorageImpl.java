package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
user.getFriends().addAll(getFriends(user.getId()));

        return user;
    }



    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ?"+
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return user;
    }

    @Override
    public User get(int id) {
        String sqlQuery = "SELECT user_id, email, login, name, birthday FROM users WHERE user_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, User.class, id);
    }


    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT f.*, m.name AS mpa_name, m.mpa_id FROM film AS f INNER JOIN mpa AS m ON f.mpa = m.id",
                (resultSet, rowNum) -> userBuilder(resultSet));
    }

    public User userBuilder(ResultSet resultSet) throws SQLException {
        User user = User.builder()
                .id(resultSet.getInt("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
        return user;
        //genres,likes!!!!!!!
    }
    @Override
    public void addFriend(int userId,int friendId) {
        jdbcTemplate.update("INSERT INTO friend_list(user_id,friendId)" +
                "VALUES(?,?)", userId,friendId);
    }
    @Override
    public void deleteFriend(int userId,int friendId) {
        jdbcTemplate.update("DELETE FROM friend_list WHERE user_id = ? AND friend_id = ?", userId, friendId);
    }
    @Override
    public List<User> getFriends(int userId) {
        List<Integer> friends =  jdbcTemplate.query(
                "SELECT friend_id FROM friend_list WHERE user_id = ?",
                (resultSet, rowNum) -> resultSet.getInt("friend_id"), userId);
return friends.stream()
        .map(this::get)
        .collect(Collectors.toList());
    }


}










