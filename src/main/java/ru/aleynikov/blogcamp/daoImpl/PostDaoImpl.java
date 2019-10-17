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
        String query = "SELECT post_id FROM post WHERE \"user\" = ? AND LOWER(title) = LOWER(?)";
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
    public List<Post> sortNewestPosts(int offset, int limit) {
        String query = "SELECT * FROM post ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
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
        String query = "SELECT COUNT(*) FROM post";
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        count = jdbc.queryForObject(query, Integer.class);

        return count;
    }

    @Override
    public Post findPostById(int id) {
        String query = "SELECT * FROM post WHERE post_id = ?";
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
        String query = "SELECT * FROM post WHERE LOWER(title) LIKE LOWER(?) ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
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
        String query = "SELECT COUNT(*) FROM post WHERE LOWER(title) LIKE LOWER(?)";
        Object[] qparams = new Object[] {"%"+filter+"%"};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> findPostsByTag(int offset, int limit, String tag) {
        String query = "SELECT * FROM (post_to_tag right join post using (post_id)) WHERE tag_id = (SELECT tag_id FROM tag WHERE name = ?) ORDER BY (created_date) DESC  OFFSET ? LIMIT ?;";
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
        String query = "SELECT COUNT(*) FROM (post_to_tag right join post using (post_id)) WHERE tag_id = (SELECT tag_id FROM tag WHERE name = ?)";
        Object[] qparams = new Object[] {tag};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> findPostsByUsername(int offset, int limit, String username) {
        String query = "SELECT * FROM post WHERE \"user\" = (SELECT user_id FROM usr WHERE username = ?) ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
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
        String query = "SELECT COUNT(*) FROM post WHERE \"user\" = (SELECT user_id FROM usr WHERE username = ?)";
        Object[] qparams = new Object[] {username};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> findPostsGlobal(int offset, int limit, String search) {
        String query = "SELECT post.post_id, title, text, \"user\", intro_image, created_date, banned, comments_count FROM post " +
                "JOIN post_to_tag USING (post_id) " +
                "JOIN tag USING (tag_id) " +
                "WHERE LOWER(tag.name) LIKE LOWER(?) " +
                "OR LOWER(post.title) LIKE LOWER(?) " +
                "OR LOWER(post.text) LIKE LOWER(?) " +
                "GROUP BY post.post_id " +
                "ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        String searchValue = "%" + search + "%";
        Object[] qparams = new Object[] {searchValue, searchValue, searchValue, offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public int countGlobal(String search) {
        String query = "SELECT COUNT(DISTINCT post_to_tag.post_id) " +
                "FROM post JOIN post_to_tag USING (post_id) " +
                "    JOIN tag USING (tag_id) " +
                "WHERE LOWER(tag.name) LIKE LOWER(?) " +
                "   OR LOWER(post.title) LIKE LOWER(?) " +
                "   OR LOWER(post.text) LIKE LOWER(?) ";
        String searchValue = "%" + search + "%";
        Object[] qparams = new Object[] {searchValue, searchValue, searchValue};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> findNewestPostsByUserId(int offset, int limit, int userId) {
        String query = "SELECT * FROM post WHERE \"user\" = ? AND deleted = false ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
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
        String query = "SELECT * FROM post WHERE \"user\" = ? AND deleted = false AND LOWER(title) LIKE LOWER(?) ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
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
        String query = "SELECT COUNT(*) FROM post WHERE \"user\" = ? AND deleted = false AND LOWER(title) LIKE (?)";
        Object[] qparams = new Object[] {userId, "%" + search + "%"};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }
}
