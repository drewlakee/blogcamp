package ru.aleynikov.blogcamp.daos.daoInterfeces;

import ru.aleynikov.blogcamp.domain.models.Comment;

import java.util.List;

public interface CommentDao {

    void delete(String query, Object[] qparams);
    void save(String query, Object[] qparams);

    List<Comment> queryForList(String query, Object[] qparams);

    int count(String query, Object[] qparams);
}
