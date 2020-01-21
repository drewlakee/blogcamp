package ru.aleynikov.blogcamp.daos.daoImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.daos.daoInterfeces.PostDao;
import ru.aleynikov.blogcamp.domain.modelMappers.PostRowMapper;
import ru.aleynikov.blogcamp.domain.models.Post;
import ru.aleynikov.blogcamp.domain.models.Tag;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.services.TagService;

import java.util.*;

@Component
public class PostDaoImpl implements PostDao {

    private static final Logger log = LoggerFactory.getLogger(PostDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private PostRowMapper postRowMapper;

    // TODO: Need to refactor - separate service and dao layer
    @Autowired
    private TagService tagService;

    @Override
    public int findPostIdByUserIdAndTitle(int userId, String title) {
        String query = "SELECT post_id FROM post, usr WHERE \"user\" = ? AND LOWER(title) = LOWER(?) AND deleted = false AND \"user\" = user_id AND banned = false";
        Object[] qparams = new Object[] {userId, title.toLowerCase()};
        int postIdFromDb;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            postIdFromDb = jdbc.queryForObject(query, qparams, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            postIdFromDb = -1;
        }

        return postIdFromDb;
    }

    @Override
    public void setTagsToPost(Set<String> tags, HashMap<String, Object> post) {
        String insertTagToPostQuery = "INSERT INTO post_to_tag (post_id, tag_id) VALUES (?, ?)";
        Object[] qparamsForInsert;
        int postIdFromDb = findPostIdByUserIdAndTitle((Integer) post.get("user"), (String) post.get("title"));

        if (postIdFromDb != -1) {
            for (String tag : tags) {
                Optional<Tag> tagFromDb = tagService.findTagByName(tag);

                // check tag - is it exist?
                // if exist, then do inserts
                // else save new tag, then take it from db and do inserts
                if (tagFromDb.isPresent()) {
                    qparamsForInsert = new Object[] {postIdFromDb, tagFromDb.get().getId()};
                    jdbc.update(insertTagToPostQuery, qparamsForInsert);
                } else {
                    tagService.save(tag);
                    tagFromDb = tagService.findTagByName(tag);

                    qparamsForInsert = new Object[] {postIdFromDb, tagFromDb.get().getId()};
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
            Optional<Tag> tagFromDb = tagService.findTagByName(tag);

            if (tagFromDb.isPresent()) {
                qparams = new Object[] {post.get("post_id"), tagFromDb.get().getId()};
                jdbc.update(dropQuery, qparams);
            }
        }
    }

    @Override
    public void delete(String query, Object[] qparams) {

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);
    }

    @Override
    public int count(String query, Object[] qparams) {
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Post> queryForList(String query, Object[] qparams) {
        List<Post> posts;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        posts = jdbc.query(query, qparams, postRowMapper);

        return posts;
    }

    @Override
    public Optional<Post> queryForObject(String query, Object[] qparams) {
        Optional<Post> post;

        try {
            log.info(query + ", {}", qparams);
            post = Optional.of((Post) jdbc.queryForObject(query, qparams, postRowMapper));
        } catch (EmptyResultDataAccessException e) {
            post = Optional.empty();
        }

        return post;
    }

    @Override
    public void save(String query, Object[] qparams) {
        log.info(SecurityUtils.getPrincipal().getUsername() + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void update(String query, Object[] qparams) {
        log.info(SecurityUtils.getPrincipal().getUsername() + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }
}
