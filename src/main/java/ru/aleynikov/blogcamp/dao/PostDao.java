package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.Post;

import java.util.HashMap;
import java.util.Set;

public interface PostDao {

    void savePost(HashMap<String, Object> post);
    Post findPostByUserIdAndTitle(int userId, String title);
    void setTagsToPost(Set<String> tags, HashMap<String, Object> post);
}
