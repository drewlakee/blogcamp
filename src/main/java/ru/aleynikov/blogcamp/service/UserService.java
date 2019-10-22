package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.UserDaoImpl;
import ru.aleynikov.blogcamp.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserDaoImpl userDao;

    public List<User> getSortedByUsernameUserList(int page, int usersOnPageLimit) {
        return userDao.findAscByUsername(FilterDataManager.filterOffset(page, usersOnPageLimit), usersOnPageLimit);
    }

    public List<User> getFilterByUsernameUsersList(int page, int usersOnPageLimit, String filter) {
        return userDao.findByUsername(FilterDataManager.filterOffset(page, usersOnPageLimit), usersOnPageLimit, filter);
    }

    public int getFilterUsersCount(String filter) {
        return userDao.countByUsername(filter);
    }

    public int getAllUsersCount() {
        return userDao.count();
    }

    public User findUserByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public User findById(int id) {
        return userDao.findById(id);
    }

    public void saveUser(Map<String, Object> newUser) {
        userDao.save(newUser);
    }

    public void updateUserAboutInfo(HashMap<String, Object> infoForUpdate) {

        if ((Integer) infoForUpdate.get("country") == -1) {
            infoForUpdate.put("country", null);
        }

        if ((Integer) infoForUpdate.get("city") == -1) {
            infoForUpdate.put("city", null);
        }

        userDao.updateProfile(infoForUpdate);
    }

    public void updateUserSecret(String secretQuestion, String secretAnswer, int userId) {
        userDao.updateSecret(secretQuestion, secretAnswer, userId);
    }

    public void updateUserPassword(String username, String newPassword) {
        userDao.updatePasswordByUsername(username, newPassword);
    }

    public void updateUserAvatarByUserId(String avatar, int id) {
        userDao.updateAvatarByUserId(avatar, id);
    }

    public void banById(int id) {
        userDao.banById(id);
    }

    public void unBanById(int id) {
        userDao.unBanById(id);
    }

    public List<User> findActiveUsersWithLimit(int limit) {
        return userDao.findActiveUsersWithLimit(limit);
    }
}
