package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.User;

import java.util.List;
import java.util.Map;

public interface UserDao {

    User findUserByUsername(String username);
    void addUser(Map<String, Object> newUser);
    void updateUserPassword(String username, String newPassword);
    List<User> getSortedByUsernameAscUserList(int offset, int limit);
    List<User> getFilterByUsernameUserList(int offset, int limit, String filter);
    int getAllUsersCount();
    int getFilterByUsernameCount(String filter);
}
