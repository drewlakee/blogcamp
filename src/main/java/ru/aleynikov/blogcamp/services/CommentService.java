package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daos.daoInterfeces.CommentDao;
import ru.aleynikov.blogcamp.domain.models.Comment;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDao commentDao;

    public List<Comment> getNewestCommentListByPostIdWithOffsetAndLimit(int offset, int limitLoadNewComments, int id) {
        String query = "SELECT comment_id, text, created_date, \"user\", post, deleted FROM comment, usr " +
                "WHERE \"user\" = user_id " +
                "AND usr.banned = false " +
                "AND post = ? " +
                "AND deleted = false " +
                "ORDER BY (created_date) DESC " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {id, offset, limitLoadNewComments};

        return commentDao.queryForList(query, qparams);
    }

    public void save(HashMap<String, Object> comment) {
        String query = "INSERT INTO comment (text, created_date, \"user\", post) VALUES (?, ?, ?, ?)";

        if (comment.get("created_date") == null) {
            comment.replace("created_date", new Timestamp(System.currentTimeMillis()));
        }

        Object[] qparams = new Object[] {
                comment.get("text"),
                comment.get("created_date"),
                comment.get("user_id"),
                comment.get("post_id")
        };

        commentDao.save(query, qparams);
    }

    public int countByPostId(int id) {
        String query = "SELECT COUNT(*) FROM comment, usr WHERE user_id = \"user\" AND banned = false AND post = ? AND deleted = false";
        Object[] qparams = new Object[] {id};

        return commentDao.count(query, qparams);
    }

    public void deleteById(int id) {
        String query = "UPDATE comment SET deleted = true " +
                "WHERE comment_id = ? " +
                "AND deleted = false";
        Object[] qparams = new Object[] {id};


        commentDao.delete(query, qparams);
    }
}
