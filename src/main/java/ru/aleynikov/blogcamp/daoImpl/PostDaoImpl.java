package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.PostDao;
import ru.aleynikov.blogcamp.mapper.PostRowMapper;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.security.SecurityUtils;
import ru.aleynikov.blogcamp.service.TagService;

import java.util.Arrays;
import java.util.HashMap;
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
                post.get("created_date")
        };

        log.info(SecurityUtils.getPrincipal().getUsername() + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public Post findPostByUserIdAndTitle(int userId, String title) {
        String query = "SELECT * FROM post WHERE \"user\" = ? AND title = LOWER(?)";
        Object[] qparams = new Object[] {userId, title.toLowerCase()};
        Post post = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            post = (Post) jdbc.queryForObject(query, qparams, postRowMapper);
        } catch (NullPointerException e) {}

        return post;
    }

    @Override
    public void setTagsToPost(Set<String> tags, HashMap<String, Object> post) {
        String insertTagToPostQuery = "INSERT INTO post_to_tag (post_id, tag_id) VALUES (?, ?)";
        Object[] qparamsForInsert;
        Post postFromDb = findPostByUserIdAndTitle((Integer) post.get("user"), (String) post.get("title"));

        if (postFromDb != null) {
            for (String tag : tags) {
                Tag tagFromDb = tagService.findTagByName(tag);

                if (tagFromDb != null) {
                    qparamsForInsert = new Object[] {postFromDb.getId(), tagFromDb.getId()};
                    jdbc.update(insertTagToPostQuery, qparamsForInsert);
                } else {
                    tagService.saveTag(tag);
                    tagFromDb = tagService.findTagByName(tag);

                    qparamsForInsert = new Object[] {postFromDb.getId(), tagFromDb.getId()};
                    jdbc.update(insertTagToPostQuery, qparamsForInsert);
                }
            }
        } else
            log.error(post.toString() + " not found.");
    }
}
