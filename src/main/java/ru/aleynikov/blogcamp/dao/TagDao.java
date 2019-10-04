package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface TagDao {

    Tag findTagByName(String name);
    void saveTag(String name);
    List<Tag> sortByPostCountTagsList(int offset, int limit);
    List<Tag> sortByCreatedDateNewestTagsList(int offset, int limit);
    int count();
    List<Tag> searchByNameTagsList(int offset, int limit, String filter);
    int countByName(String filter);
    List<Tag> findTagsByPostId(int id);
}
