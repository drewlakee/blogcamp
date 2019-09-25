package ru.aleynikov.blogcamp.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper {

    @Override
    public User mapRow(ResultSet rs, int i) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setSecretQuestion(rs.getString("secret_question"));
        user.setSecretAnswer(rs.getString("secret_answer"));
        user.setActive(rs.getBoolean("active"));
        if (rs.getDate("registered") != null)
            user.setRegisteredDate(rs.getDate("registered").toLocalDate());
        else
            user.setRegisteredDate(null);
        user.setAbout(rs.getString("about"));
        if (rs.getDate("birthday") != null)
            user.setBirthday(rs.getDate("birthday").toLocalDate());
        else
            user.setBirthday(null);
        user.setCountry(rs.getString("country"));
        user.setCity(rs.getString("city"));
        user.setFullName(rs.getString("fullname"));
        user.setRole(rs.getString("role"));
        user.setAvatar(rs.getString("avatar"));

        return user;
    }
}
