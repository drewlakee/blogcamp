package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.TagDao;
import ru.aleynikov.blogcamp.model.Tag;
import ru.aleynikov.blogcamp.rowMapper.TagRowMapper;

import java.util.Arrays;
import java.util.List;

@Component
public class TagDaoImpl implements TagDao {

    private static final Logger log = LoggerFactory.getLogger(TagDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Override
    public List<Tag> getSortedByPostCountTagsList(int offset, int limit) {
        String querySortedTagList = "SELECT * FROM tag ORDER BY post_count DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {offset, limit};
        List<Tag> tagList;

        log.info(querySortedTagList + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(querySortedTagList, qparams, new TagRowMapper());

        return tagList;
    }

    @Override
    public List<Tag> getSortedByCreatedDateNewestTagsList(int offset, int limit) {
        String querySortedTagList = "SELECT * FROM tag ORDER BY created DESC OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] {offset, limit};
        List<Tag> tagList;


        log.info(querySortedTagList + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(querySortedTagList, qparams, new TagRowMapper());

        return tagList;
    }

    @Override
    public int getTagsCount() {
        String queryTagsCount = "SELECT COUNT(*) FROM tag";

        log.info(queryTagsCount);
        int count = jdbc.queryForObject(queryTagsCount, Integer.class);

        return count;
    }

    @Override
    public List<Tag> getSearchByNameTagsList(int offset, int limit, String filter) {
        String querySearchTagList = "SELECT * FROM tag WHERE LOWER(name) LIKE LOWER(?) OFFSET ? LIMIT ?";
        Object[] qparams = new Object[] { "%"+filter+"%", offset, limit};
        List<Tag> tagList;

        log.info(querySearchTagList + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(querySearchTagList, qparams, new TagRowMapper());

        return tagList;
    }

    @Override
    public int getSearchByNameCount(String filter) {
        String queryCountSearchByNameTags = "SELECT COUNT(*) FROM tag WHERE name LIKE ?";
        Object[] qparams = new Object[] {"%"+filter+"%"};

        log.info(queryCountSearchByNameTags);
        int count = jdbc.queryForObject(queryCountSearchByNameTags, qparams, Integer.class);

        return count;
    }
}
