package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.PostDaoImpl;

import java.util.HashMap;
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
}
