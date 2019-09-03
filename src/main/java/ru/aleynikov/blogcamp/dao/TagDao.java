package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.Tag;

import java.util.List;

public interface TagDao {

    List<Tag> getSortedByPostCountTagsList(int offset, int limit);
    List<Tag> getSortedByCreatedDateNewestTagsList(int offset, int limit);
    int getTagsCount();
}
