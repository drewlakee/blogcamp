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
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRowMapper userRowMapper;

    private static final String userMainInfoQuery = "SELECT user_id, username, password, fullname, secret_question, secret_answer, active, online, role, registered, about, birthday, country.name AS \"country\", city.name AS \"city\" FROM usr LEFT JOIN country ON country.country_id = usr.country LEFT JOIN city ON city.city_id = usr.city";

    @Override
    public User findUserByUsername(String username) {
        String query = userMainInfoQuery + " WHERE usr.username = ?";
        Object[] qparams = new Object[] { username };

        User user = null;
        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            user = (User) jdbc.queryForObject(query, qparams, userRowMapper);
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
        String query = userMainInfoQuery + " ORDER BY (username) ASC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {offset, limit};
        List<User> userList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        userList = jdbc.query(query, qparams, userRowMapper);

        return userList;
    }

    @Override
    public List<User> getFilterByUsernameUserList(int offset, int limit, String filter) {
        String query = userMainInfoQuery + " WHERE LOWER(username) LIKE LOWER(?) OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {"%"+filter+"%", offset, limit};
        List<User> userList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        userList = jdbc.query(query, qparams, userRowMapper);

        return userList;
    }

    @Override
    public int getAllUsersCount() {
        String query = "SELECT COUNT(*) FROM usr";

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        int count = jdbc.queryForObject(query, Integer.class);

        return count;
    }

    @Override
    public int getFilterByUsernameCount(String filter) {
        String query = "SELECT COUNT(*) FROM usr WHERE username LIKE ?";
        Object[] qparams = new Object[] {"%"+filter+"%"};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        int count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }
}
