package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.TagDao;
import ru.aleynikov.blogcamp.mapper.PostRowMapper;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.mapper.TagRowMapper;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

@Component
public class TagDaoImpl implements TagDao {

    private static final Logger log = LoggerFactory.getLogger(TagDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private TagRowMapper tagRowMapper;

    @Override
    public List<Tag> getSortedByPostCountTagsList(int offset, int limit) {
        String query = "SELECT * FROM tag ORDER BY post_count DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {offset, limit};
        List<Tag> tagList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(query, qparams, tagRowMapper);

        return tagList;
    }

    @Override
    public List<Tag> getSortedByCreatedDateNewestTagsList(int offset, int limit) {
        String query = "SELECT * FROM tag ORDER BY created DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {offset, limit};
        List<Tag> tagList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(query, qparams, tagRowMapper);

        return tagList;
    }

    @Override
    public int getTagsCount() {
        String query = "SELECT COUNT(*) FROM tag";

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        int count = jdbc.queryForObject(query, Integer.class);

        return count;
    }

    @Override
    public List<Tag> getSearchByNameTagsList(int offset, int limit, String filter) {
        String query = "SELECT * FROM tag WHERE LOWER(name) LIKE LOWER(?) OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] { "%"+filter+"%", offset, limit};
        List<Tag> tagList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(query, qparams, tagRowMapper);

        return tagList;
    }

    @Override
    public int getFilterByNameCount(String filter) {
        String query = "SELECT COUNT(*) FROM tag WHERE name LIKE ?";
        Object[] qparams = new Object[] {"%"+filter+"%"};
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
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
    public void saveTag(String name) {
        String query = "INSERT INTO tag (name) VALUES (?)";
        Object[] qparams = new Object[] {name};

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        jdbc.update(query, qparams);
    }
}
