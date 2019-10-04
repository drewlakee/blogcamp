package ru.aleynikov.blogcamp.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.model.Post;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("post_id"));
        post.setTitle(rs.getString("title"));
        post.setText(rs.getString("text"));
        post.setIntroImage(rs.getString("intro_image"));
        post.setCreatedDate(rs.getDate("created_date"));
        post.setBanned(rs.getBoolean("banned"));

        return post;
    }
}
