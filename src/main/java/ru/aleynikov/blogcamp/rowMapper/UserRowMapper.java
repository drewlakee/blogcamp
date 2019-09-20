package ru.aleynikov.blogcamp.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.aleynikov.blogcamp.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("user_id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setSecretQuestion(resultSet.getString("secret_question"));
        user.setSecretAnswer(resultSet.getString("secret_answer"));
        user.setActive(resultSet.getBoolean("active"));
        user.setRegisteredDate(resultSet.getDate("registered"));
        user.setAbout(resultSet.getString("about"));
        user.setBirthday(resultSet.getDate("birthday"));
        user.setCountry(resultSet.getString("country"));
        user.setCity(resultSet.getString("city"));
        user.setFullName(resultSet.getString("fullname"));
        user.setRole(resultSet.getString("role"));
        user.setBanned(resultSet.getBoolean("isbanned"));

        return user;
    }
}
