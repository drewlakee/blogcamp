package ru.aleynikov.blogcamp.domain.modelMappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.domain.models.Role;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.services.CityService;
import ru.aleynikov.blogcamp.services.CountryService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class UserRowMapper implements RowMapper {

    @Autowired
    private CityService cityService;

    @Autowired
    private CountryService countryService;

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

        user.setCountry((countryService.findById(rs.getInt("country"))));
        user.setCity(cityService.findCityById(rs.getInt("city")));
        user.setFullName(rs.getString("fullname"));
        user.setRole(Role.getRoleEnum(rs.getString("role")));
        user.setAvatar(rs.getString("avatar"));
        user.setBanned(rs.getBoolean("banned"));

        return user;
    }
}
