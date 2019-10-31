package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpls.CommentDaoImpl;
import ru.aleynikov.blogcamp.models.Comment;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDaoImpl commentDao;

    public List<Comment> findNewestByPostIdWithOffsetAndLimit(int offset, int limitLoadNewComments, int id) {
        return commentDao.findNewestByPostIdWithOffsetAndLimit(offset, limitLoadNewComments, id);
    }

    public void save(HashMap<String, Object> comment) {
        if (comment.get("created_date") == null) {
            comment.replace("created_date", new Timestamp(System.currentTimeMillis()));
        }

        commentDao.save(comment);
    }

    public int countByPostId(int id) {
        return commentDao.countByPostId(id);
    }

    public void deleteById(int id) {
        commentDao.deleteById(id);
    }
}
