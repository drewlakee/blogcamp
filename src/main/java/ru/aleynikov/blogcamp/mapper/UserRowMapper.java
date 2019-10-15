package ru.aleynikov.blogcamp.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.model.Role;
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

        if (rs.getTimestamp("registered") != null)
            user.setRegisteredDate(rs.getTimestamp("registered"));
        else
            user.setRegisteredDate(null);

        user.setStatus(rs.getString("status"));

        if (rs.getTimestamp("birthday") != null)
            user.setBirthday(rs.getDate("birthday"));
        else
            user.setBirthday(null);

        user.setCountry(rs.getString("country"));
        user.setCity(rs.getString("city"));
        user.setFullName(rs.getString("fullname"));
        user.setRole(Role.getRoleEnum(rs.getString("role")));
        user.setAvatar(rs.getString("avatar"));
        user.setBanned(rs.getBoolean("banned"));

        return user;
    }
}
