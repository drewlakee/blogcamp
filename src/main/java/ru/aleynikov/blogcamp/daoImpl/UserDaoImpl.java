package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.UserDao;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.mapper.UserRowMapper;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDaoImpl implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRowMapper userRowMapper;

    private static final String userMainInfoQuery = "SELECT user_id, username, password, fullname, secret_question, secret_answer, active, role, registered, about, birthday, avatar, country.name AS \"country\", city.name AS \"city\" FROM usr LEFT JOIN country ON country.country_id = usr.country LEFT JOIN city ON city.city_id = usr.city";

    @Override
    public User findUserByUsername(String username) {
        String query = userMainInfoQuery + " WHERE usr.username = ?";
        Object[] qparams = new Object[] { username };
        User user = null;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            user = (User) jdbc.queryForObject(query, qparams, userRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return user;
    }

    @Override
    public User findUserById(int id) {
        String query = userMainInfoQuery + " WHERE user_id = ?";
        Object[] qparams = new Object[] {id};
        User user = null;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            user = (User) jdbc.queryForObject(query, qparams, userRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return user;
    }

    @Override
    public void saveUser(Map<String, Object> user) {
        String query = "INSERT INTO usr (username, password, secret_question, secret_answer, avatar) VALUES (?, ?, ?, ?, ?)";
        Object[] qparams = new Object[] {
                user.get("username"),
                user.get("password"),
                user.get("secret_question"),
                user.get("secret_answer"),
                user.get("avatar")};

        log.info(query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
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
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        count = jdbc.queryForObject(query, Integer.class);

        return count;
    }

    @Override
    public int getFilterByUsernameCount(String filter) {
        String query = "SELECT COUNT(*) FROM usr WHERE username LIKE ?";
        Object[] qparams = new Object[] {"%"+filter+"%"};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public void updateUserAboutInfo(HashMap<String, Object> infoForUpdate) {
        String query = "UPDATE usr SET fullname=?, birthday=?, country=?, city=?, about=? WHERE user_id=?";
        Object[] qparams = infoForUpdate.values().toArray();

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void updateUserSecret(String secretQuestion, String secretAnswer, int userId) {
        String query = "UPDATE usr SET secret_question=?, secret_answer=? WHERE user_id=?";
        Object[] qparams = new Object[] {secretQuestion, secretAnswer, userId};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }
}
