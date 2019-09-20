package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.UserDaoImpl;
import ru.aleynikov.blogcamp.model.User;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDaoImpl userDao;

    private int filterOffset(int page) {
        int offset = 0;

        if (page == 1 || page < 0)
            page = 0;

        if (page > 0) {
            offset += 36 * (page - 1);
        }

        return offset;
    }

    public List<User> getSortedByUsernameUserList(int page, int usersOnPageLimit) {

        return userDao.getSortedByUsernameAscUserList(filterOffset(page), usersOnPageLimit);
    }

    public List<User> getFilterByUsernameUsersList(int page, int usersOnPageLimit, String filter) {

        return userDao.getFilterByUsernameUserList(filterOffset(page), usersOnPageLimit, filter);
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
}
