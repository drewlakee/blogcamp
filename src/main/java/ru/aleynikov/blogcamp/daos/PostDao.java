package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.Post;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public interface PostDao {

    void save(HashMap<String, Object> post);
    void deleteById(int id);
    void updatePost(HashMap<String, Object> post);

    int findPostIdByUserIdAndTitle(int userId, String title);
    Post findPostById(int id);
    List<Post> findPostsByTitle(int offset, int limit, String filter);
    List<Post> findPostsByTag(int offset, int limit, String tag);
    List<Post> findPostsByUsername(int offset, int limit, String username);
    List<Post> findPostsGlobal(int offset, int limit, String search);
    List<Post> findNewestPostsByUserId(int offset, int limit, int userId);
    List<Post> findNewestPostsByUserIdAndSearchByTitle(int offset, int limit, int userId, String search);
    List<Post> findInterestingPosts(int offset, int limit);
    List<Post> findNewestPosts(int offset, int limit);
    List<Post> findInterestingPostsWithLimit(int limit);

    void setTagsToPost(Set<String> tags, HashMap<String, Object> post);
    void removeTagsFromPost(Set<String> tags, HashMap<String, Object> post);

    int count(String query, Object[] qparams);
}
