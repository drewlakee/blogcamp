package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.Comment;

import java.util.HashMap;
import java.util.List;

public interface CommentDao {

    void save(HashMap<String, Object> comment);
    void deleteById(int id);

    List<Comment> findNewestByPostIdWithOffsetAndLimit(int offset, int limit, int id);

    int countByPostId(int id);
}
