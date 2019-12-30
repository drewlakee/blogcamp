package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daos.daoInterfeces.UserDao;
import ru.aleynikov.blogcamp.domain.models.User;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> getSortedByUsernameUsersList(int page, int usersOnPageLimit, boolean isAdmin) {
        String bannedUsers = isAdmin ? "" : "WHERE banned = false";
        String query = "SELECT * FROM usr " + bannedUsers + " " +
                "ORDER BY (username) ASC " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {
                DataHandleService.filterOffset(page, usersOnPageLimit),
                usersOnPageLimit
        };

        return userDao.queryForList(query, qparams);
    }

    public List<User> getFilteredByUsernameUsersList(int page, int usersOnPageLimit, String filter, boolean isAdmin) {
        String bannedUsers = isAdmin ? "" : "AND banned = false";
        String query = "SELECT * FROM usr " +
                "WHERE LOWER(username) LIKE LOWER(?) " + bannedUsers + " " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {
                "%"+filter+"%",
                DataHandleService.filterOffset(page, usersOnPageLimit),
                usersOnPageLimit
        };

        return userDao.queryForList(query, qparams);
    }

    public int getFilteredUsersCount(String filter) {
        String query = "SELECT COUNT(*) FROM usr " +
                "WHERE username LIKE ?";
        Object[] qparams = new Object[] {"%"+filter+"%"};

        return userDao.count(query, qparams);
    }

    public int getAllUsersCount() {
        String query = "SELECT COUNT(*) FROM usr";

        return userDao.count(query, null);
    }

    public User findUserByUsername(String username) {
        String query = "SELECT * FROM usr " +
                "WHERE LOWER(usr.username) = LOWER(?) " +
                "AND banned = false";
        Object[] qparams = new Object[] { username };

        return userDao.queryForObject(query, qparams);
    }

    public User findUserById(int id) {
        String query = "SELECT * FROM usr WHERE user_id = ?";
        Object[] qparams = new Object[] {id};

        return userDao.queryForObject(query, qparams);
    }

    public void save(Map<String, Object> newUser) {
        String query = "INSERT INTO usr (username, password, secret_question, secret_answer, avatar, registered) VALUES (?, ?, ?, ?, ?, ?)";
        Object[] qparams = new Object[] {
                newUser.get("username"),
                newUser.get("password"),
                newUser.get("secret_question"),
                newUser.get("secret_answer"),
                newUser.get("avatar"),
                new Timestamp(System.currentTimeMillis()) // registered - date
        };

        userDao.save(query, qparams);
    }

    public void updateUserAboutInfo(HashMap<String, Object> infoForUpdate) {
        String query = "UPDATE usr SET fullname=?, birthday=?, country=?, city=?, status=? " +
                "WHERE user_id=?";

        if ((Integer) infoForUpdate.get("country") == -1) {
            infoForUpdate.put("country", null);
        }

        if ((Integer) infoForUpdate.get("city") == -1) {
            infoForUpdate.put("city", null);
        }

        Object[] qparams = infoForUpdate.values().toArray();

        userDao.update(query, qparams);
    }

    public void updateUserSecret(String secretQuestion, String secretAnswer, int userId) {
        String query = "UPDATE usr SET secret_question=?, secret_answer=? " +
                "WHERE user_id=?";
        Object[] qparams = new Object[] {secretQuestion, secretAnswer, userId};

        userDao.update(query, qparams);
    }

    public void updateUserPassword(String username, String newPassword) {
        String query = "UPDATE usr SET password=? " +
                "WHERE username=?";
        Object[] qparams = new Object[] {newPassword, username};

        userDao.update(query, qparams);
    }

    public void updateUserAvatarByUserId(String avatar, int id) {
        String query = "UPDATE usr SET avatar= ? " +
                "WHERE user_id = ?";
        Object[] qparams = new Object[] {avatar, id};

        userDao.update(query, qparams);
    }

    public void banUserById(int id) {
        String query = "UPDATE usr SET banned = true, active = false " +
                "WHERE user_id = ?";
        Object[] qparams = new Object[] {id};

        userDao.ban(query, qparams);
    }

    public void unbanUserById(int id) {
        String query = "UPDATE usr SET banned = false, active = true " +
                "WHERE user_id = ?";
        Object[] qparams = new Object[] {id};

        userDao.unban(query, qparams);
    }

    public List<User> getActiveUsersWithLimit(int limit) {
        String query = "SELECT *, (SELECT COUNT(*)  FROM post " +
                "WHERE deleted = false AND usr.user_id = \"user\") as count FROM usr " +
                "WHERE banned = false " +
                "AND (SELECT COUNT(*)  FROM post " +
                "WHERE deleted = false AND usr.user_id = \"user\") > 0 " +
                "ORDER BY count DESC " +
                "OFFSET 0 LIMIT ? ";
        Object[] qparams = new Object[] {limit};

        return userDao.queryForList(query, qparams);
    }
}
