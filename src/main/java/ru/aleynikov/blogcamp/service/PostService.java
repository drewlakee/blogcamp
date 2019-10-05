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

    public List<Post> sortNewestPostsByUserId(int id, int page, int postsLimitOnPage) {
        return postDao.sortNewestPostsByUserId(id, FilterDataManager.filterOffset(page, postsLimitOnPage), postsLimitOnPage);
    }

    public int countPostsByUserId(int id) {
        return postDao.countPostsByUserId(id);
    }
}
