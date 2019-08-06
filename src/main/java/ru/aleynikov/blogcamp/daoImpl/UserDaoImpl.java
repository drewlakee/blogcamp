package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.UserDao;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.rowMapper.UserRowMapper;

import java.util.Arrays;

@Component
public class UserDaoImpl implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public User findUserByUsername(String username) {
        User user = null;
        Object[] qparams = new Object[] { username };
        String query = "SELECT * from usr where username = ?";

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            user = (User) jdbc.queryForObject(query, qparams, new UserRowMapper());
        } catch (Exception e) {
            log.warn("User not found with username {}", username);
        }

        return user;
    }

}
