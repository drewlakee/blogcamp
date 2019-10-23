package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserDao {

    void save(Map<String, Object> user);
    void banById(int id);
    void unBanById(int id);

    void updateProfile(HashMap<String, Object> infoForUpdate);
    void updateSecret(String secretQuestion, String secretAnswer, int userId);
    void updateAvatarByUserId(String avatar, int id);
    void updatePasswordByUsername(String username, String newPassword);

    User findByUsername(String username);
    User findById(int id);
    List<User> findAscByUsername(int offset, int limit, boolean isAdmin);
    List<User> findByUsername(int offset, int limit, String filter, boolean isAdmin);
    List<User> findActiveUsersWithLimit(int limit);

    int count();
    int countByUsername(String filter);
}
