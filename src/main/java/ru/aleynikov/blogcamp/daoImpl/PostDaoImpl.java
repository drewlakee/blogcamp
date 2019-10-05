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
    public void savePost(HashMap<String, Object> post) {
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
    public List<Post> sortNewestPostsByUserId(int id, int offset, int limit) {
        String query = "SELECT * FROM post WHERE \"user\" = ? ORDER BY (created_date) DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {id, offset, limit};
        List<Post> posts = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            posts = jdbc.query(query, qparams, postRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return posts;
    }

    @Override
    public int countPostsByUserId(int id) {
        String query = "SELECT COUNT(*) FROM post WHERE \"user\" = ?";
        Object[] qparams = new Object[] {id};
        int count;

        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }
}
