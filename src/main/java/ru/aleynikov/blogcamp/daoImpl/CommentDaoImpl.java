package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.CommentDao;
import ru.aleynikov.blogcamp.mapper.CommentRowMapper;
import ru.aleynikov.blogcamp.model.Comment;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.sql.Timestamp;
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
        String query = "SELECT * FROM comment WHERE post = ? ORDER BY (created_date) OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {id, offset, limit};
        List<Comment> comments;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        comments = jdbc.query(query, qparams, commentRowMapper);

        return comments;
    }

    @Override
    public void save(HashMap<String, Object> comment) {
        String query = "INSERT INTO comment (comment_id, text, created_date, \"user\", post) VALUES (?, ?, ?, ?, ?)";
        Object[] qparams = new Object[] {
                comment.get("comment_id"),
                comment.get("text"),
                new Timestamp(System.currentTimeMillis()),
                comment.get("user_id"),
                comment.get("post_id")
        };

        log.info(SecurityUtils.getPrincipal().getUsername() + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }
}
