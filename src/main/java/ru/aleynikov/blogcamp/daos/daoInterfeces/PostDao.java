package ru.aleynikov.blogcamp.daos.daoInterfeces;

import ru.aleynikov.blogcamp.domain.models.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface PostDao {

    void save(String query, Object[] qparams);
    void delete(int id);
    void update(String query, Object[] qparams);

    int findPostIdByUserIdAndTitle(int userId, String title);

    Post queryForObject(String query, Object[] qparams);
    List<Post> queryForList(String query, Object[] qparams);

    void setTagsToPost(Set<String> tags, HashMap<String, Object> post);
    void removeTagsFromPost(Set<String> tags, HashMap<String, Object> post);

    int count(String query, Object[] qparams);
}
