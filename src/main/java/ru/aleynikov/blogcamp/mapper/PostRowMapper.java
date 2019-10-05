package ru.aleynikov.blogcamp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.model.Post;
import ru.aleynikov.blogcamp.service.TagService;
import ru.aleynikov.blogcamp.service.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostRowMapper implements RowMapper {

    @Autowired
    private TagService tagService;

    @Autowired
    private UserService userService;

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post post = new Post();
        post.setId(rs.getInt("post_id"));
        post.setTitle(rs.getString("title"));
        post.setText(rs.getString("text"));
        post.setIntroImage(rs.getString("intro_image"));
        post.setCreatedDate(rs.getTimestamp("created_date"));
        post.setBanned(rs.getBoolean("banned"));
        post.setTags(tagService.findTagsByPostId(post.getId()));
        post.setUser(userService.findUserById(rs.getInt("user")));

        return post;
    }
}
