package ru.aleynikov.blogcamp.dao;

import java.util.HashMap;

public interface PostDao {

    void savePost(HashMap<String, Object> post);
}
