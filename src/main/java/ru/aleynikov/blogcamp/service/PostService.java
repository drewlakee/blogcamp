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
        postDao.savePost(post);
    }

    public void setTagsToPost(Set<String> tags, HashMap<String, Object> post) {
        postDao.setTagsToPost(tags, post);
    }

    public List<Post> sortNewestByUserId(int id, int page, int postsLimitOnPage) {
        return postDao.sortNewestPostsByUserId(id, FilterDataManager.filterOffset(page, postsLimitOnPage), postsLimitOnPage);
    }

    public int countByUserId(int id) {
        return postDao.countByUserId(id);
    }

    public List<Post> sortOldestByUserId(int id, int page, int postsLimitOnPage) {
        return postDao.sortOldestPostsByUserId(id, FilterDataManager.filterOffset(page, postsLimitOnPage), postsLimitOnPage);
    }

    public List<Post> findByTitleUsingUserId(int id, int page, int postsLimitOnPage, String filter) {
        return postDao.findPostsByTitleUsingUserId(id, FilterDataManager.filterOffset(page, postsLimitOnPage), postsLimitOnPage, filter);
    }

    public int countByFilterUsingUserId(int id, String filter) {
        return postDao.countByFilterUsingUserId(id, filter);
    }

    public Post findPostById(int id) {
        return postDao.findPostById(id);
    }

    public List<Post> sortNewestPosts(int page, int postsLimitOnPage) {
        return postDao.sortNewestPosts(FilterDataManager.filterOffset(page, postsLimitOnPage), postsLimitOnPage);
    }

    public int count() {
        return postDao.count();
    }

    public List<Post> findPostsByTitle(int page, int postsLimitOnPage, String filter) {
        return postDao.findPostsByTitle(FilterDataManager.filterOffset(page, postsLimitOnPage), postsLimitOnPage, filter);
    }

    public int countByTitle(String filter) {
        return postDao.countByTitle(filter);
    }

    public List<Post> findPostsByTag(int page, int postsLimitOnPage, String tag) {
        return postDao.findPostsByTag(FilterDataManager.filterOffset(page, postsLimitOnPage), postsLimitOnPage, tag);
    }

    public int countByTag(String tag) {
        return postDao.countByTag(tag);
    }

    public List<Post> findByUsername(int page, int postsLimitOnPage, String username) {
        return postDao.findPostsByUsername(FilterDataManager.filterOffset(page, postsLimitOnPage), postsLimitOnPage, username);
    }

    public int countByUsername(String username) {
        return postDao.countByUsername(username);
    }
}
