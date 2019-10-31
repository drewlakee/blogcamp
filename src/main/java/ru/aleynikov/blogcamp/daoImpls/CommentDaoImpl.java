package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.daos.CommentDao;
import ru.aleynikov.blogcamp.mappers.CommentRowMapper;
import ru.aleynikov.blogcamp.models.Comment;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class CommentDaoImpl implements CommentDao {

    private static final Logger log = LoggerFactory.getLogger(CommentDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CommentRowMapper commentRowMapper;

    @Override
    public List<Comment> findNewestByPostIdWithOffsetAndLimit(int offset, int limit, int id) {
        String query = "SELECT comment_id, text, created_date, \"user\", post, deleted FROM comment, usr WHERE \"user\" = user_id AND usr.banned = false AND post = ? AND deleted = false ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {id, offset, limit};
        List<Comment> comments;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        comments = jdbc.query(query, qparams, commentRowMapper);

        return comments;
    }
    
    @Override
    public void save(HashMap<String, Object> comment) {
        String query = "INSERT INTO comment (text, created_date, \"user\", post) VALUES (?, ?, ?, ?)";
        Object[] qparams = new Object[] {
                comment.get("text"),
                comment.get("created_date"),
                comment.get("user_id"),
                comment.get("post_id")
        };

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public int countByPostId(int id) {
        String query = "SELECT COUNT(*) FROM comment, usr WHERE user_id = \"user\" AND banned = false AND post = ? AND deleted = false";
        Object[] qparams = new Object[] {id};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public void deleteById(int id) {
        String query = "UPDATE comment SET deleted = true WHERE comment_id = ? AND deleted = false";
        Object[] qparams = new Object[] {id};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);
    }
}
