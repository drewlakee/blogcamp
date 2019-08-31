package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.Tag;

import java.util.List;

public interface TagDao {

    List<Tag> getSortedByPostCountDescTagsList(int offset, int limit);
}
