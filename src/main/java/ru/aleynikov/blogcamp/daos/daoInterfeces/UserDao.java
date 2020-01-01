package ru.aleynikov.blogcamp.daos.daoInterfeces;

import ru.aleynikov.blogcamp.domain.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    void save(String query, Object[] qparams);
    void ban(String query, Object[] qprams);
    void unban(String query, Object[] qparams);
    void update(String query, Object[] qparams);

    Optional<User> queryForObject(String query, Object[] qparams);
    List<User> queryForList(String query, Object[] qparams);

    int count(String query, Object[] qparams);
}
