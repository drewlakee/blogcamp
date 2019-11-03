package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserDao {

    void save(String query, Object[] qparams);
    void ban(String query, Object[] qprams);
    void unban(String query, Object[] qparams);
    void update(String query, Object[] qparams);

    User queryForObject(String query, Object[] qparams);
    List<User> queryForList(String query, Object[] qparams);

    int count(String query, Object[] qparams);
}
