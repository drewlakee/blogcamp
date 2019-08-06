package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.User;

public interface UserDao {

    User findUserByUsername(String username);
}
