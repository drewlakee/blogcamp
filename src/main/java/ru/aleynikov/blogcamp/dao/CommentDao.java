package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.Comment;

import java.util.HashMap;
import java.util.List;

public interface CommentDao {

    List<Comment> findNewestByPostIdWithOffsetAndLimit(int offset, int limit, int id);
    void save(HashMap<String, Object> comment);
    int countByPostId(int id);
}
