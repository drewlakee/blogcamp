package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.User;

import java.util.Map;

public interface UserDao {

    User findUserByUsername(String username);
    void addUser(Map<String, Object> newUser);
    void updateUserPassword(String username, String newPassword);
}
