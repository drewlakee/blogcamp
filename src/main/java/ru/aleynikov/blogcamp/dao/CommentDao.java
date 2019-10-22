package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.Comment;

import java.util.HashMap;
import java.util.List;

public interface CommentDao {

    void save(HashMap<String, Object> comment);
    void deleteById(int id);

    List<Comment> findNewestByPostIdWithOffsetAndLimit(int offset, int limit, int id);

    int countByPostId(int id);
}
