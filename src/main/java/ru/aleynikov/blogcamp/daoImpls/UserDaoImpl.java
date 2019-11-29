package ru.aleynikov.blogcamp.daoImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.daos.UserDao;
import ru.aleynikov.blogcamp.mappers.UserRowMapper;
import ru.aleynikov.blogcamp.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class UserDaoImpl implements UserDao {

    private static Logger log = LoggerFactory.getLogger(UserDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private UserRowMapper userRowMapper;

    @Override
    public void update(String query, Object[] qparams) {
        log.info(query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public int count(String query, Object[] qparams) {
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public User queryForObject(String query, Object[] qparams) {
        User user;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            user = (User) jdbc.queryForObject(query, qparams, userRowMapper);
        } catch (EmptyResultDataAccessException e) {
            user = null;
        }

        return user;
    }

    @Override
    public List<User> queryForList(String query, Object[] qparams) {
        List<User> userList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        userList = jdbc.query(query, qparams, userRowMapper);

        return userList;
    }

    @Override
    public void save(String query, Object[] qparams) {
        log.info(query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void ban(String query, Object[] qparams) {
        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);
    }

    @Override
    public void unban(String query, Object[] qparams) {
        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);
    }
}
