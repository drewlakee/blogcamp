package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.CommentDaoImpl;
import ru.aleynikov.blogcamp.model.Comment;

import java.util.HashMap;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentDaoImpl commentDao;

    public List<Comment> findNewestByPostIdWithOffsetAndLimit(int page, int commentsOnPage, int id) {
        return commentDao.findNewestByPostIdWithOffsetAndLimit(FilterDataManager.filterOffset(page, commentsOnPage), commentsOnPage, id);
    }

    public void save(HashMap<String, Object> comment) {
        commentDao.save(comment);
    }
}
