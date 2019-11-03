package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.Tag;

import java.util.List;

public interface TagDao {

    void save(String query, Object[] qparams);
    void update(String query, Object[] qparams);

    List<Tag> queryForList(String query, Object[] qparams);
    Tag queryForObject(String query, Object[] qparams);

    int count(String query, Object[] qparams);
}
