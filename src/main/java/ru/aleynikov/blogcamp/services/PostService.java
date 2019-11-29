package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpls.PostDaoImpl;
import ru.aleynikov.blogcamp.daos.PostDao;
import ru.aleynikov.blogcamp.models.Post;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    private static final String mainPostQueryParams = " post_id, title, text, \"user\", intro_image, created_date, comments_count, deleted ";

    public void savePost(HashMap<String, Object> post) {
        String query = "INSERT INTO post (title, text, \"user\", intro_image, created_date) VALUES (?, ?, ?, ?, ?)";
        Object[] qparams = new Object[] {
                post.get("title"),
                post.get("text"),
                post.get("user"),
                post.get("intro_image"),
                new Timestamp(System.currentTimeMillis()) // created_date
        };

        postDao.save(query, qparams);
    }

    public void setTagsToPost(Set<String> tags, HashMap<String, Object> post) {
        postDao.setTagsToPost(tags, post);
    }

    public Post findPostById(int id) {
        String query = "SELECT " + mainPostQueryParams + " " +
                "FROM post, usr " +
                "WHERE post_id = ? " +
                "AND deleted = false " +
                "AND \"user\" = user_id " +
                "AND usr.banned = false";
        Object[] qparams = new Object[] {
                id
        };

        return postDao.queryForObject(query, qparams);
    }

    public List<Post> getSortedNewestPostList(int page, int componentLimit) {
        String query = "SELECT " + mainPostQueryParams + " " +
                "FROM post, usr " +
                "WHERE deleted = false " +
                "AND \"user\" = user_id " +
                "AND usr.banned = false " +
                "ORDER BY (created_date) DESC " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {
                FilterDataManager.filterOffset(page, componentLimit),
                componentLimit
        };

        return postDao.queryForList(query, qparams);
    }

    public int count() {
        String query = "SELECT COUNT(*) FROM post, usr " +
                "WHERE deleted = false " +
                "AND \"user\" = user_id " +
                "AND usr.banned = false";


        return postDao.count(query, null);
    }

    public List<Post> getPostListByTitle(int page, int componentLimit, String filter) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr WHERE LOWER(title) LIKE LOWER(?) AND deleted = false AND \"user\" = user_id AND usr.banned = false ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {
                "%"+filter+"%",
                FilterDataManager.filterOffset(page, componentLimit),
                componentLimit
        };

        return postDao.queryForList(query, qparams);
    }

    public int countByTitle(String filter) {
        String query = "SELECT COUNT(*) " +
                "FROM post, usr " +
                "WHERE LOWER(title) LIKE LOWER(?) " +
                "AND deleted = false " +
                "AND \"user\" = user_id " +
                "AND usr.banned = false";
        Object[] qparams = new Object[] {"%"+filter+"%"};

        return postDao.count(query, qparams);
    }

    public List<Post> getPostListByTag(int page, int componentLimit, String tag) {
        String query = "SELECT " + mainPostQueryParams + " FROM (post_to_tag right join post using (post_id)), usr WHERE tag_id = (SELECT tag_id FROM tag WHERE name = ?) AND \"user\" = user_id AND usr.banned = false AND deleted = false ORDER BY (created_date) DESC  OFFSET ? LIMIT ?;";
        Object[] qparams = new Object[] {
                tag,
                FilterDataManager.filterOffset(page, componentLimit),
                componentLimit
        };

        return postDao.queryForList(query, qparams);
    }

    public int countByTag(String tag) {
        String query = "SELECT COUNT(*) " +
                "FROM (post_to_tag right join post using (post_id)), usr " +
                "WHERE tag_id = (SELECT tag_id " +
                "FROM tag WHERE name = ?) " +
                "AND \"user\" = user_id " +
                "AND usr.banned = false " +
                "AND deleted = false";
        Object[] qparams = new Object[] {tag};

        return postDao.count(query, qparams);
    }

    public List<Post> getPostListByUsername(int page, int componentLimit, String username) {
        String query = "SELECT " + mainPostQueryParams + " " +
                "FROM post " +
                "WHERE \"user\" = (SELECT user_id FROM usr WHERE username = ? AND usr.banned = false) " +
                "AND deleted = false " +
                "ORDER BY (created_date) DESC " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {
                username,
                FilterDataManager.filterOffset(page, componentLimit),
                componentLimit
        };

        return postDao.queryForList(query, qparams);
    }

    public int countPostsByUsername(String username) {
        String query = "SELECT COUNT(*) " +
                "FROM post " +
                "WHERE \"user\" = (SELECT user_id FROM usr WHERE username = ? " +
                "AND usr.banned = false) " +
                "AND deleted = false";
        Object[] qparams = new Object[] {username};

        return postDao.count(query, qparams);
    }

    public List<Post> getPostListGlobal(int page, int componentLimit, String search) {
        String query = "SELECT post.post_id, title, text, \"user\", intro_image, created_date, comments_count, deleted FROM post " +
                "JOIN post_to_tag USING (post_id) " +
                "JOIN tag USING (tag_id) " +
                "JOIN usr ON \"user\" = user_id " +
                "WHERE post.deleted = false " +
                "AND usr.banned = false " +
                "AND (LOWER(tag.name) LIKE LOWER(?) " +
                "OR LOWER(post.title) LIKE LOWER(?) " +
                "OR LOWER(post.text) LIKE LOWER(?) " +
                "OR LOWER(username) LIKE LOWER(?)) " +
                "GROUP BY post.post_id " +
                "ORDER BY (created_date) DESC " +
                "OFFSET ? LIMIT ?";
        String searchValue = "%" + search + "%";
        Object[] qparams = new Object[] {
                searchValue,
                searchValue,
                searchValue,
                searchValue,
                FilterDataManager.filterOffset(page, componentLimit),
                componentLimit};

        return postDao.queryForList(query, qparams);
    }

    public int countGlobal(String search) {
        String query = "SELECT COUNT(DISTINCT post_to_tag.post_id) FROM post " +
                "JOIN post_to_tag USING (post_id) " +
                "JOIN tag USING (tag_id) " +
                "JOIN usr ON \"user\" = user_id " +
                "WHERE post.deleted = false " +
                "AND usr.banned = false " +
                "AND (LOWER(tag.name) LIKE LOWER(?) " +
                "OR LOWER(post.title) LIKE LOWER(?) " +
                "OR LOWER(post.text) LIKE LOWER(?) " +
                "OR LOWER(username) LIKE LOWER(?))";
        String searchValue = "%" + search + "%";
        Object[] qparams = new Object[] {searchValue, searchValue, searchValue, searchValue};

        return postDao.count(query, qparams);
    }

    public List<Post> getNewestPostListByUserId(int offset, int componentLimit, int id) {
        String query = "SELECT " + mainPostQueryParams + " " +
                "FROM post, usr " +
                "WHERE \"user\" = ? " +
                "AND \"user\" = user_id " +
                "AND usr.banned = false " +
                "AND deleted = false " +
                "ORDER BY (created_date) DESC " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {id, offset, componentLimit};

        return postDao.queryForList(query, qparams);
    }

    public List<Post> getNewestPostListByUserIdAndSearchByTitle(int page, int componentLimit, int id, String search) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr WHERE \"user\" = ? AND \"user\" = user_id AND usr.banned = false AND deleted = false AND LOWER(title) LIKE LOWER(?) ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {
                id,
                "%" + search + "%" ,
                FilterDataManager.filterOffset(page, componentLimit),
                componentLimit
        };

        return postDao.queryForList(query, qparams);
    }

    public int countByUserIdAndSearchByTitle(int id, String search) {
        String query = "SELECT COUNT(*) " +
                "FROM post, usr " +
                "WHERE \"user\" = ? " +
                "AND \"user\" = user_id " +
                "AND usr.banned = false " +
                "AND deleted = false " +
                "AND LOWER(title) LIKE (?)";
        Object[] qparams = new Object[] {id, "%" + search + "%"};

        return postDao.count(query, qparams);
    }

    public void deleteById(int id) {
        postDao.delete(id);
    }

    public void removeTagsFromPost(Set<String> tags, HashMap<String, Object> post) {
        postDao.removeTagsFromPost(tags, post);
    }

    public void update(HashMap<String, Object> post) {
        String query = "UPDATE post SET title = ?, text = ?, intro_image = ?, created_date = ? WHERE post_id = ?";
        Object[] qparams = new Object[] {
                post.get("title"),
                post.get("text"),
                post.get("intro_image"),
                post.get("created_date"),
                post.get("post_id")
        };

        postDao.update(query, qparams);
    }

    public List<Post> getInterestingPostList(int page, int componentLimit) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr " +
                "WHERE user_id = \"user\" " +
                "AND usr.banned = false " +
                "AND deleted = false " +
                "ORDER BY (comments_count) DESC " +
                "OFFSET ? LIMIT ? ";
        Object[] qparams = new Object[] {
                FilterDataManager.filterOffset(page, componentLimit),
                componentLimit
        };

        return postDao.queryForList(query, qparams);
    }

    public List<Post> getInterestingPostListWithLimit(int limit) {
        String query = "SELECT  post_id, title, text, \"user\", intro_image, created_date, comments_count, deleted FROM post, usr " +
                "WHERE \"user\" = user_id " +
                "AND banned = false " +
                "AND deleted = false " +
                "ORDER BY comments_count DESC " +
                "OFFSET 0 LIMIT ? ";
        Object[] qparams = new Object[] {
                limit
        };

        return postDao.queryForList(query, qparams);
    }
}
