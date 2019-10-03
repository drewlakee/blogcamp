package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface TagDao {

    Tag findTagByName(String name);
    void saveTag(String name);
    List<Tag> getSortedByPostCountTagsList(int offset, int limit);
    List<Tag> getSortedByCreatedDateNewestTagsList(int offset, int limit);
    int getTagsCount();
    List<Tag> getSearchByNameTagsList(int offset, int limit, String filter);
    int getFilterByNameCount(String filter);
}
