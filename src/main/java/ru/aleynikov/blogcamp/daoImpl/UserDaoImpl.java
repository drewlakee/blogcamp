package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.UserDao;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.rowMapper.UserRowMapper;

import java.util.Arrays;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public User findUserByUsername(String username) {
        Object[] qparams = new Object[] { username };
        String query = "SELECT * FROM usr WHERE username = ?";

        User user = null;
        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            user = (User) jdbc.queryForObject(query, qparams, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            log.info("User not found with username [{}]", username);
        }

        return user;
    }

    @Override
    public void addUser(Map<String, Object> newUser) {
        String insertNewUserQuery = "INSERT INTO usr (username, password, secret_question, secret_answer) VALUES " +
                "(?, ?, ?, ?)";

        Object[] newUserData = new Object[] {newUser.get("username"), newUser.get("password"),
                newUser.get("secret_question"), newUser.get("secret_answer")};
        log.info(insertNewUserQuery + ", {}", Arrays.toString(newUserData));
        jdbc.update(insertNewUserQuery, newUserData);
    }

    @Override
    public void updateUserPassword(String username, String newPassword) {
        String updatePassQuery = "UPDATE usr SET password=? WHERE username=?";

        Object[] userData = new Object[] {newPassword, username};
        log.info(updatePassQuery + ", {}", Arrays.toString(userData));
        jdbc.update(updatePassQuery, userData);
    }
}
