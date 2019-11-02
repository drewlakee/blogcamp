package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpls.PostDaoImpl;
import ru.aleynikov.blogcamp.models.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Service
public class PostService {

    @Autowired
    private PostDaoImpl postDao;

    public void savePost(HashMap<String, Object> post) {
        postDao.save(post);
    }

    public void setTagsToPost(Set<String> tags, HashMap<String, Object> post) {
        postDao.setTagsToPost(tags, post);
    }

    public Post findById(int id) {
        return postDao.findPostById(id);
    }

    public List<Post> sortNewestPosts(int page, int componentLimit) {
        return postDao.findNewestPosts(FilterDataManager.filterOffset(page, componentLimit), componentLimit);
    }

    public int count() {
        String query = "SELECT COUNT(*) FROM post, usr " +
                "WHERE deleted = false " +
                "AND \"user\" = user_id " +
                "AND usr.banned = false";


        return postDao.count(query, null);
    }

    public List<Post> findPostsByTitle(int page, int componentLimit, String filter) {
        return postDao.findPostsByTitle(FilterDataManager.filterOffset(page, componentLimit), componentLimit, filter);
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

    public List<Post> findPostsByTag(int page, int componentLimit, String tag) {
        return postDao.findPostsByTag(FilterDataManager.filterOffset(page, componentLimit), componentLimit, tag);
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

    public List<Post> findPostsByUsername(int page, int componentLimit, String username) {
        return postDao.findPostsByUsername(FilterDataManager.filterOffset(page, componentLimit), componentLimit, username);
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

    public List<Post> findPostsGlobal(int page, int componentLimit, String search) {
        return postDao.findPostsGlobal(FilterDataManager.filterOffset(page, componentLimit), componentLimit, search);
    }

    public int countGlobal(String search) {
        String query = "SELECT COUNT(DISTINCT post_to_tag.post_id) " +
                "FROM post JOIN post_to_tag USING (post_id) " +
                "JOIN tag USING (tag_id) " +
                "JOIN usr ON post.\"user\" = user_id " +
                "WHERE (LOWER(tag.name) LIKE LOWER(?) " +
                "OR LOWER(post.title) LIKE LOWER(?) " +
                "OR LOWER(post.text) LIKE LOWER(?)) " +
                "OR LOWER(username) LIKE LOWER(?) " +
                "AND deleted = false " +
                "AND usr.banned = false ";
        String searchValue = "%" + search + "%";
        Object[] qparams = new Object[] {searchValue, searchValue, searchValue, searchValue};

        return postDao.count(query, qparams);
    }

    public List<Post> findNewestPostsByUserId(int offset, int componentLimit, int id) {
        return postDao.findNewestPostsByUserId(offset, componentLimit, id);
    }

    public List<Post> findNewestPostsByUserIdAndSearchByTitle(int page, int componentLimit, int id, String search) {
        return postDao.findNewestPostsByUserIdAndSearchByTitle(page, componentLimit, id, search);
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
        postDao.deleteById(id);
    }

    public void removeTagsFromPost(Set<String> tags, HashMap<String, Object> post) {
        postDao.removeTagsFromPost(tags, post);
    }

    public void updatePost(HashMap<String, Object> post) {
        postDao.updatePost(post);
    }

    public List<Post> findInterestingPosts(int page, int componentLimit) {
        return postDao.findInterestingPosts(FilterDataManager.filterOffset(page, componentLimit), componentLimit);
    }

    public List<Post> findInterestingPostsWithLimit(int limit) {
        return postDao.findInterestingPostsWithLimit(limit);
    }
}
