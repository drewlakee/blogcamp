package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.UserDao;
import ru.aleynikov.blogcamp.mapper.UserRowMapper;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.sql.Timestamp;
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

    @Override
    public User findByUsername(String username) {
        String query = "SELECT * FROM usr WHERE LOWER(usr.username) = LOWER(?) AND banned = false";
        Object[] qparams = new Object[] { username };
        User user = null;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            user = (User) jdbc.queryForObject(query, qparams, userRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return user;
    }

    @Override
    public User findById(int id) {
        String query = "SELECT * FROM usr WHERE user_id = ?";
        Object[] qparams = new Object[] {id};
        User user = null;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            user = (User) jdbc.queryForObject(query, qparams, userRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return user;
    }

    @Override
    public void save(Map<String, Object> user) {
        String query = "INSERT INTO usr (username, password, secret_question, secret_answer, avatar, registered) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] qparams = new Object[] {
                user.get("username"),
                user.get("password"),
                user.get("secret_question"),
                user.get("secret_answer"),
                user.get("avatar"),
                new Timestamp(System.currentTimeMillis())
        };

        log.info(query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void updatePasswordByUsername(String username, String newPassword) {
        String query = "UPDATE usr SET password=? WHERE username=?";
        Object[] userData = new Object[] {newPassword, username};

        log.info(query + ", {}", Arrays.toString(userData));
        jdbc.update(query, userData);
    }

    @Override
    public List<User> findAscByUsername(int offset, int limit) {
        String query = "SELECT * FROM usr ORDER BY (username) ASC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {offset, limit};
        List<User> userList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        userList = jdbc.query(query, qparams, userRowMapper);

        return userList;
    }

    @Override
    public List<User> findByUsername(int offset, int limit, String filter) {
        String query = "SELECT * FROM usr WHERE LOWER(username) LIKE LOWER(?) OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {"%"+filter+"%", offset, limit};
        List<User> userList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        userList = jdbc.query(query, qparams, userRowMapper);

        return userList;
    }

    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM usr";
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        count = jdbc.queryForObject(query, Integer.class);

        return count;
    }

    @Override
    public int countByUsername(String filter) {
        String query = "SELECT COUNT(*) FROM usr WHERE username LIKE ?";
        Object[] qparams = new Object[] {"%"+filter+"%"};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public void updateProfile(HashMap<String, Object> infoForUpdate) {
        String query = "UPDATE usr SET fullname=?, birthday=?, country=?, city=?, status=? WHERE user_id=?";
        Object[] qparams = infoForUpdate.values().toArray();

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void updateSecret(String secretQuestion, String secretAnswer, int userId) {
        String query = "UPDATE usr SET secret_question=?, secret_answer=? WHERE user_id=?";
        Object[] qparams = new Object[] {secretQuestion, secretAnswer, userId};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void updateAvatarByUserId(String avatar, int id) {
        String query = "UPDATE usr SET avatar= ? WHERE user_id = ?";
        Object[] qparams = new Object[] {avatar, id};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void banById(int id) {
        String query = "UPDATE usr SET banned = true, active = false WHERE user_id = ?";
        Object[] qparams = new Object[] {id};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);
    }

    @Override
    public void unBanById(int id) {
        String query = "UPDATE usr SET banned = false, active = true WHERE user_id = ?";
        Object[] qparams = new Object[] {id};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);
    }

    @Override
    public List<User> findActiveUsersWithLimit(int limit) {
        String query = "SELECT *, (SELECT COUNT(*) FROM post " +
                " WHERE deleted = false AND usr.user_id = \"user\") as count FROM usr" +
                " WHERE banned = false" +
                " ORDER BY count DESC" +
                " OFFSET 0 LIMIT ?";
        Object[] qparams = new Object[] {limit};
        List<User> userList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        userList = jdbc.query(query, qparams, userRowMapper);

        return userList;
    }
}
