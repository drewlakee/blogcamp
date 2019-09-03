package ru.aleynikov.blogcamp.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import ru.aleynikov.blogcamp.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TagRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Tag tag = new Tag();
        tag.setId(rs.getInt("tag_id"));
        tag.setName(rs.getString("name"));
        tag.setDescription(rs.getString("description"));
        tag.setPostCount(rs.getInt("post_count"));
        tag.setCreatedDate(rs.getDate("created"));

        return tag;
    }
}
