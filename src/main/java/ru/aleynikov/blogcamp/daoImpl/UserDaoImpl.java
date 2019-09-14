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
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public User findUserByUsername(String username) {
        String query = "SELECT * FROM usr WHERE LOWER(username) = LOWER(?)";
        Object[] qparams = new Object[] { username };

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
        String query = "INSERT INTO usr (username, password, secret_question, secret_answer) VALUES " +
                "(?, ?, ?, ?)";

        Object[] newUserData = new Object[] {newUser.get("username"), newUser.get("password"),
                newUser.get("secret_question"), newUser.get("secret_answer")};
        log.info(query + ", {}", Arrays.toString(newUserData));
        jdbc.update(query, newUserData);
    }

    @Override
    public void updateUserPassword(String username, String newPassword) {
        String query = "UPDATE usr SET password=? WHERE username=?";

        Object[] userData = new Object[] {newPassword, username};
        log.info(query + ", {}", Arrays.toString(userData));
        jdbc.update(query, userData);
    }

    @Override
    public List<User> getSortedByUsernameAscUserList(int offset, int limit) {
        String query = "SELECT * FROM usr ORDER BY (username) ASC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {offset, limit};
        List<User> userList;

        log.info(query + ", {}", Arrays.toString(qparams));
        userList = jdbc.query(query, qparams, new UserRowMapper());

        return userList;
    }

    @Override
    public List<User> getFilterByUsernameUserList(int offset, int limit, String filter) {
        String query = "SELECT * FROM usr WHERE LOWER(username) LIKE LOWER(?) OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {"%"+filter+"%", offset, limit};
        List<User> userList;

        log.info(query + ", {}", Arrays.toString(qparams));
        userList = jdbc.query(query, qparams, new UserRowMapper());

        return userList;
    }

    @Override
    public int getAllUsersCount() {
        String query = "SELECT COUNT(*) FROM usr";

        log.info(query);
        int count = jdbc.queryForObject(query, Integer.class);

        return count;
    }

    @Override
    public int getFilterByUsernameCount(String filter) {
        String query = "SELECT COUNT(*) FROM usr WHERE username LIKE ?";
        Object[] qparams = new Object[] {"%"+filter+"%"};

        log.info(query + ", {}", qparams);
        int count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }
}
