package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserDao {

    User findByUsername(String username);
    User findById(int id);
    void save(Map<String, Object> user);
    void updatePasswordByUsername(String username, String newPassword);
    List<User> sortAscByUsername(int offset, int limit);
    List<User> findByUsername(int offset, int limit, String filter);
    int count();
    int countByUsername(String filter);
    void updateProfile(HashMap<String, Object> infoForUpdate);
    void updateSecret(String secretQuestion, String secretAnswer, int userId);
    void updateAvatarByUserId(String avatar, int id);
    void banById(int id);
    void unBanById(int id);
}
