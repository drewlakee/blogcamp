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

        return userDao.getSortedByUsernameAscUserList(FilterDataManger.filterOffset(page, usersOnPageLimit), usersOnPageLimit);
    }

    public List<User> getFilterByUsernameUsersList(int page, int usersOnPageLimit, String filter) {

        return userDao.getFilterByUsernameUserList(FilterDataManger.filterOffset(page, usersOnPageLimit), usersOnPageLimit, filter);
    }

    public int getFilterUsersCount(String filter) {

        return userDao.getFilterByUsernameCount(filter);
    }

    public int getAllUsersCount() {

        return userDao.getAllUsersCount();
    }

    public User findUserByUsername(String username) {
        User user = userDao.findUserByUsername(username);

        return user;
    }

    public void saveUser(Map<String, Object> newUser) {
        userDao.saveUser(newUser);
    }

    public void updateUserAboutInfo(HashMap<String, Object> infoForUpdate) {
        if ((Integer) infoForUpdate.get("country") == -1) {
            infoForUpdate.put("country", null);
        }

        if ((Integer) infoForUpdate.get("city") == -1) {
            infoForUpdate.put("city", null);
        }

        userDao.updateUserAboutInfo(infoForUpdate);
    }

    public void updateUserSecret(String secretQuestion, String secretAnswer, int userId) {
        userDao.updateUserSecret(secretQuestion, secretAnswer, userId);
    }

    public void updateUserPassword(String username, String newPassword) {
        userDao.updateUserPassword(username, newPassword);
    }
}
