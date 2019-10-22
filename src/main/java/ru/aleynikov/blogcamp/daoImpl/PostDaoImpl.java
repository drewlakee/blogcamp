package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.PostDao;
import ru.aleynikov.blogcamp.mapper.PostRowMapper;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.TagService;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class PostDaoImpl implements PostDao {

    private static final Logger log = LoggerFactory.getLogger(PostDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private PostRowMapper postRowMapper;

    @Autowired
    private TagService tagService;


    private static final String mainPostQueryParams = " post_id, title, text, \"user\", intro_image, created_date, comments_count, deleted ";

    @Override
    public void save(HashMap<String, Object> post) {
        String query = "INSERT INTO post (title, text, \"user\", intro_image, created_date) VALUES(?, ?, ?, ?, ?)";
        Object[] qparams = new Object[] {
                post.get("title"),
                post.get("text"),
                post.get("user"),
                post.get("intro_image"),
                new Timestamp(System.currentTimeMillis())
        };

        log.info(SecurityUtils.getPrincipal().getUsername() + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public int findPostIdByUserIdAndTitle(int userId, String title) {
        String query = "SELECT post_id FROM post, usr WHERE \"user\" = ? AND LOWER(title) = LOWER(?) AND deleted = false AND \"user\" = user_id AND banned = false";
        Object[] qparams = new Object[] {userId, title.toLowerCase()};
        int postIdFromDb = -1;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            postIdFromDb = jdbc.queryForObject(query, qparams, Integer.class);
        } catch (EmptyResultDataAccessException e) {}

        return postIdFromDb;
    }

    @Override
    public void setTagsToPost(Set<String> tags, HashMap<String, Object> post) {
        String insertTagToPostQuery = "INSERT INTO post_to_tag (post_id, tag_id) VALUES (?, ?)";
        Object[] qparamsForInsert;
        int postIdFromDb = findPostIdByUserIdAndTitle((Integer) post.get("user"), (String) post.get("title"));

        if (postIdFromDb != -1) {
            for (String tag : tags) {
                Tag tagFromDb = tagService.findTagByName(tag);

                if (tagFromDb != null) {
                    qparamsForInsert = new Object[] {postIdFromDb, tagFromDb.getId()};
                    jdbc.update(insertTagToPostQuery, qparamsForInsert);
                } else {
                    tagService.saveTag(tag);
                    tagFromDb = tagService.findTagByName(tag);

                    qparamsForInsert = new Object[] {postIdFromDb, tagFromDb.getId()};
                    jdbc.update(insertTagToPostQuery, qparamsForInsert);
                }
            }
        } else
            log.error(post.toString() + " not found.");
    }

    @Override
    public void removeTagsFromPost(Set<String> tags, HashMap<String, Object> post) {
        String dropQuery = "DELETE FROM post_to_tag WHERE post_id = ? AND tag_id = ?";
        Object[] qparams;

        for (String tag : tags) {
            Tag tagFromDb = tagService.findTagByName(tag);
            qparams = new Object[] {post.get("post_id"), tagFromDb.getId()};
            jdbc.update(dropQuery, qparams);
        }
    }

    @Override
    public List<Post> findNewestPosts(int offset, int limit) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr WHERE deleted = false AND \"user\" = user_id AND usr.banned = false ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM post, usr WHERE deleted = false AND \"user\" = user_id AND usr.banned = false";
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        count = jdbc.queryForObject(query, Integer.class);

        return count;
    }

    @Override
    public Post findPostById(int id) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr WHERE post_id = ? AND deleted = false AND \"user\" = user_id AND usr.banned = false";
        Object[] qparams = new Object[] {id};
        Post post = null;

        try {
            log.info(query + ", {}", qparams);
            post = (Post) jdbc.queryForObject(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return post;
    }

    @Override
    public List<Post> findPostsByTitle(int offset, int limit, String filter) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr WHERE LOWER(title) LIKE LOWER(?) AND deleted = false AND \"user\" = user_id AND usr.banned = false ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {"%"+filter+"%", offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public int countByTitle(String filter) {
        String query = "SELECT COUNT(*) FROM post, usr WHERE LOWER(title) LIKE LOWER(?) AND deleted = false AND \"user\" = user_id AND usr.banned = false";
        Object[] qparams = new Object[] {"%"+filter+"%"};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> findPostsByTag(int offset, int limit, String tag) {
        String query = "SELECT " + mainPostQueryParams + " FROM (post_to_tag right join post using (post_id)), usr WHERE tag_id = (SELECT tag_id FROM tag WHERE name = ?) AND \"user\" = user_id AND usr.banned = false AND deleted = false ORDER BY (created_date) DESC  OFFSET ? LIMIT ?;";
        Object[] qparams = new Object[] {tag, offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public int countByTag(String tag) {
        String query = "SELECT COUNT(*) FROM (post_to_tag right join post using (post_id)), usr WHERE tag_id = (SELECT tag_id FROM tag WHERE name = ?) AND \"user\" = user_id AND usr.banned = false AND deleted = false";
        Object[] qparams = new Object[] {tag};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> findPostsByUsername(int offset, int limit, String username) {
        String query = "SELECT " + mainPostQueryParams + " FROM post WHERE \"user\" = (SELECT user_id FROM usr WHERE username = ? AND usr.banned = false) AND deleted = false ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {username, offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public int countByUsername(String username) {
        String query = "SELECT COUNT(*) FROM post WHERE \"user\" = (SELECT user_id FROM usr WHERE username = ? AND usr.banned = false) AND deleted = false";
        Object[] qparams = new Object[] {username};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> findPostsGlobal(int offset, int limit, String search) {
        String query = "SELECT post.post_id, title, text, \"user\", intro_image, created_date, comments_count, deleted FROM post" +
                "                JOIN post_to_tag USING (post_id)" +
                "                JOIN tag USING (tag_id)" +
                "                JOIN usr ON \"user\" = user_id" +
                "                WHERE (LOWER(tag.name) LIKE LOWER(?)" +
                "                OR LOWER(post.title) LIKE LOWER(?)" +
                "                OR LOWER(post.text) LIKE LOWER(?))" +
                "                OR LOWER(username) LIKE LOWER(?)" +
                "                AND deleted = false" +
                "                AND usr.banned = false" +
                "                GROUP BY post.post_id" +
                "                ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        String searchValue = "%" + search + "%";
        Object[] qparams = new Object[] {searchValue, searchValue, searchValue, searchValue, offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public int countGlobal(String search) {
        String query = "SELECT COUNT(DISTINCT post_to_tag.post_id)" +
                "                FROM post JOIN post_to_tag USING (post_id)" +
                "                JOIN tag USING (tag_id)" +
                "                JOIN usr ON post.\"user\" = user_id" +
                "                WHERE (LOWER(tag.name) LIKE LOWER(?)" +
                "                OR LOWER(post.title) LIKE LOWER(?)" +
                "                OR LOWER(post.text) LIKE LOWER(?))" +
                "                OR LOWER(username) LIKE LOWER(?)" +
                "                AND deleted = false" +
                "                AND usr.banned = false";
        String searchValue = "%" + search + "%";
        Object[] qparams = new Object[] {searchValue, searchValue, searchValue, searchValue};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> findNewestPostsByUserId(int offset, int limit, int userId) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr WHERE \"user\" = ? AND \"user\" = user_id AND usr.banned = false AND deleted = false ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {userId, offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public List<Post> findNewestPostsByUserIdAndSearchByTitle(int offset, int limit, int userId, String search) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr WHERE \"user\" = ? AND \"user\" = user_id AND usr.banned = false AND deleted = false AND LOWER(title) LIKE LOWER(?) ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {userId, "%" + search + "%" , offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public int countByUserIdAndSearchByTitle(int userId, String search) {
        String query = "SELECT COUNT(*) FROM post, usr WHERE \"user\" = ? AND \"user\" = user_id AND usr.banned = false AND deleted = false AND LOWER(title) LIKE (?)";
        Object[] qparams = new Object[] {userId, "%" + search + "%"};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public void deleteById(int id) {
        String query = "UPDATE post SET deleted = true WHERE post_id = ?";
        Object[] qparams = new Object[] {id};

        Post postForTagsCountsUpdate = findPostById(id);

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);

        tagService.updateTagsCountsOfPostByPostId(postForTagsCountsUpdate);
    }

    @Override
    public void updatePost(HashMap<String, Object> post) {
        String query = "UPDATE post SET title = ?, text = ?, intro_image = ?, created_date = ? WHERE post_id = ?";
        Object[] qparams = new Object[] {
                post.get("title"),
                post.get("text"),
                post.get("intro_image"),
                post.get("created_date"),
                post.get("post_id")
        };

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public List<Post> findInterestingPosts(int offset, int limit) {
        String query = "SELECT " + mainPostQueryParams + " FROM post, usr " +
                "WHERE user_id = \"user\" " +
                "AND usr.banned = false " +
                "AND deleted = false " +
                "ORDER BY (comments_count) DESC " +
                "OFFSET ? LIMIT ? ";
        Object[] qparams = new Object[] {offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public List<Post> findInterestingPostsWithLimit(int limit) {
        String query = "SELECT  post_id, title, text, \"user\", intro_image, created_date, comments_count, deleted FROM post, usr " +
                "WHERE \"user\" = user_id " +
                "AND banned = false " +
                "AND deleted = false " +
                "ORDER BY comments_count DESC " +
                "OFFSET 0 LIMIT ? ";
        Object[] qparams = new Object[] {limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }
}
