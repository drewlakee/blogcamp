package ru.aleynikov.blogcamp.daoImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.daos.TagDao;
import ru.aleynikov.blogcamp.mappers.TagRowMapper;
import ru.aleynikov.blogcamp.models.Tag;
import ru.aleynikov.blogcamp.security.SecurityUtils;

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
    public void save(String query, Object[] qparams) {
        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public void update(String query, Object[] qparams) {
        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        jdbc.update(query, qparams);
    }

    @Override
    public int count(String query, Object[] qparams) {
        int count;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", qparams);
        count = jdbc.queryForObject(query, qparams, Integer.class);

        return count;
    }

    @Override
    public List<Tag> queryForList(String query, Object[] qparams) {
        List<Tag> tagList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        tagList = jdbc.query(query, qparams, tagRowMapper);

        return tagList;
    }

    @Override
    public Tag queryForObject(String query, Object[] qparams) {
        Tag tag;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            tag = (Tag) jdbc.queryForObject(query, qparams, tagRowMapper);
        } catch (EmptyResultDataAccessException e) {
            tag = null;
        }

        return tag;
    }
}
