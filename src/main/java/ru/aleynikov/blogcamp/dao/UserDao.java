package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserDao {

    User findUserByUsername(String username);
    User findUserById(int id);
    void saveUser(Map<String, Object> user);
    void updateUserPassword(String username, String newPassword);
    List<User> sortByUsernameAscUserList(int offset, int limit);
    List<User> findByUsernameUserList(int offset, int limit, String filter);
    int count();
    int countFilterByUsername(String filter);
    void updateUserAboutInfo(HashMap<String, Object> infoForUpdate);
    void updateUserSecret(String secretQuestion, String secretAnswer, int userId);
    void updateUserAvatarByUserId(String avatar, int id);
}
