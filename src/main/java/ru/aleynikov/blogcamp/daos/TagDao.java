package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.Post;
import ru.aleynikov.blogcamp.models.Tag;

import java.util.List;

public interface TagDao {

    void save(String name);

    void updateDescriptionById(String description, int id);
    void updateTagsCountsOfPostByPostId(Post post);

    Tag findTagByName(String name);
    List<Tag> findNewestTags(int offset, int limit);
    List<Tag> findByNameTagsList(int offset, int limit, String filter);
    List<Tag> findTagsByPostId(int id);
    List<Tag> findTagsSortedByNameAsc(int offset, int limit);
    List<Tag> findPopularTagsWithLimit(int limit);

    int count();
    int countByName(String filter);
}
