package ru.aleynikov.blogcamp.daos.daoImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.daos.daoInterfeces.CommentDao;
import ru.aleynikov.blogcamp.domain.modelMappers.CommentRowMapper;
import ru.aleynikov.blogcamp.domain.models.Comment;
import ru.aleynikov.blogcamp.domain.models.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.*;

@Component
public class CommentDaoImpl implements CommentDao {

    private static final Logger log = LoggerFactory.getLogger(CommentDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CommentRowMapper commentRowMapper;

    @Override
    public int count(String query, Object[] qparams) {
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Comment> queryForList(String query, Object[] qparams) {
        List<Comment> comments;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        comments = jdbc.query(query, qparams, commentRowMapper);

        return comments;
    }

    @Override
    public Optional queryForObject(String query, Object[] qparams) {
        Optional<Comment> comment;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            comment = Optional.of((Comment) jdbc.queryForObject(query, qparams, commentRowMapper));
        } catch (EmptyResultDataAccessException e) {
            comment = Optional.empty();
        }

        return comment;
    }

    @Override
    public void save(String query, Object[] qparams) {
        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void delete(String query, Object[] qparams) {
        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);
    }
}
