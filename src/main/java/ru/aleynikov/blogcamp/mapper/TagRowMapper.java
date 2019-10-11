package ru.aleynikov.blogcamp.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TagRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getInt("tag_id"));
        tag.setName(rs.getString("name"));
        tag.setDescription(rs.getString("description"));
        tag.setPostCount(rs.getInt("post_count"));
        tag.setCreatedDate(rs.getTimestamp("created"));

        return tag;
    }
}
