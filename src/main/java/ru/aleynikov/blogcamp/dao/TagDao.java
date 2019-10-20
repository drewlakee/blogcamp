package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.Tag;

import java.util.List;

public interface TagDao {

    Tag findTagByName(String name);
    void save(String name);
    List<Tag> sortByCreatedDateNewestTagsList(int offset, int limit);
    int count();
    List<Tag> findByNameTagsList(int offset, int limit, String filter);
    int countByName(String filter);
    List<Tag> findTagsByPostId(int id);
    void updateDescriptionById(String description, int id);
    void updateTagsCountsOfPostByPostId(Post post);
}
