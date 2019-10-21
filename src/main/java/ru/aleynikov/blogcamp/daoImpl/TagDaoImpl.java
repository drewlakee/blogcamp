package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.TagDao;
import ru.aleynikov.blogcamp.mapper.TagRowMapper;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Component
public class TagDaoImpl implements TagDao {

    private static final Logger log = LoggerFactory.getLogger(TagDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private TagRowMapper tagRowMapper;

    @Override
    public List<Tag> sortByCreatedDateNewestTagsList(int offset, int limit) {
        String query = "SELECT tag.tag_id, tag.name, tag.description, tag.created FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false " +
                "ORDER BY created DESC OFFSET ? LIMIT ? ";
        Object[] qparams = new Object[] {offset, limit};
        List<Tag> tagList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(query, qparams, tagRowMapper);

        return tagList;
    }

    @Override
    public int count() {
        String query = "SELECT COUNT(*) FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false ";

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        int count = jdbc.queryForObject(query, Integer.class);

        return count;
    }

    @Override
    public List<Tag> findByNameTagsList(int offset, int limit, String filter) {
        String query = "SELECT tag.tag_id, tag.name, tag.description, tag.created FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false " +
                "WHERE LOWER(tag.name) LIKE LOWER(?) " +
                "ORDER BY (created) DESC " +
                "OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] { "%"+filter+"%", offset, limit};
        List<Tag> tagList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(query, qparams, tagRowMapper);

        return tagList;
    }

    @Override
    public int countByName(String filter) {
        String query = "SELECT COUNT(*) FROM tag " +
                "JOIN post_to_tag ON tag.tag_id = post_to_tag.tag_id " +
                "JOIN post ON post_to_tag.post_id = post.post_id AND post.deleted = false " +
                "JOIN usr ON post.\"user\" = usr.user_id AND usr.banned = false " +
                "WHERE tag.name LIKE ?";
        Object[] qparams = new Object[] {"%"+filter+"%"};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public Tag findTagByName(String name) {
        String query = "SELECT * FROM tag WHERE name = ?";
        Object[] qparams = new Object[] {name};
        Tag tag = null;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            tag = (Tag) jdbc.queryForObject(query, qparams, tagRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return tag;
    }

    @Override
    public void save(String name) {
        String query = "INSERT INTO tag (name, created) VALUES (?, ?)";
        Object[] qparams = new Object[] {name, new Timestamp(System.currentTimeMillis())};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public List<Tag> findTagsByPostId(int id) {
        String query = "SELECT * FROM (post_to_tag JOIN tag USING (tag_id)) WHERE post_id = ?";
        Object[] qparams = new Object[] {id};
        List<Tag> tagList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(query, qparams, tagRowMapper);

        return tagList;
    }

    @Override
    public void updateDescriptionById(String description, int id) {
        String query = "UPDATE tag SET description = ? WHERE tag_id = ?";
        Object[] qparams = new Object[] {description, id};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void updateTagsCountsOfPostByPostId(Post post) {
        String queryInsert = "UPDATE post_to_tag SET tag_id = ? WHERE tag_id = ? AND post_id = ?";
        Object[] qparams;

        for(Tag tag : post.getTags()) {
            qparams = new Object[] {tag.getId(), tag.getId(), post.getId()};

            jdbc.update(queryInsert, qparams);
        }
    }
}
