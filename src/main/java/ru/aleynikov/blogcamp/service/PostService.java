package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.PostDaoImpl;
import ru.aleynikov.blogcamp.model.Post;

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
        return postDao.sortNewestPosts(FilterDataManager.filterOffset(page, componentLimit), componentLimit);
    }

    public int count() {
        return postDao.count();
    }

    public List<Post> findPostsByTitle(int page, int componentLimit, String filter) {
        return postDao.findPostsByTitle(FilterDataManager.filterOffset(page, componentLimit), componentLimit, filter);
    }

    public int countByTitle(String filter) {
        return postDao.countByTitle(filter);
    }

    public List<Post> findPostsByTag(int page, int componentLimit, String tag) {
        return postDao.findPostsByTag(FilterDataManager.filterOffset(page, componentLimit), componentLimit, tag);
    }

    public int countByTag(String tag) {
        return postDao.countByTag(tag);
    }

    public List<Post> findPostsByUsername(int page, int componentLimit, String username) {
        return postDao.findPostsByUsername(FilterDataManager.filterOffset(page, componentLimit), componentLimit, username);
    }

    public int countPostsByUsername(String username) {
        return postDao.countByUsername(username);
    }

    public List<Post> findPostsGlobal(int page, int componentLimit, String search) {
        return postDao.findPostsGlobal(FilterDataManager.filterOffset(page, componentLimit), componentLimit, search);
    }

    public int countGlobal(String search) {
        return postDao.countGlobal(search);
    }

    public List<Post> findNewestPostsByUserId(int offset, int componentLimit, int id) {
        return postDao.findNewestPostsByUserId(offset, componentLimit, id);
    }

    public List<Post> findNewestPostsByUserIdAndSearchByTitle(int page, int componentLimit, int id, String search) {
        return postDao.findNewestPostsByUserIdAndSearchByTitle(page, componentLimit, id, search);
    }

    public int countByUserIdAndSearchByTitle(int id, String search) {
        return postDao.countByUserIdAndSearchByTitle(id, search);
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
}
