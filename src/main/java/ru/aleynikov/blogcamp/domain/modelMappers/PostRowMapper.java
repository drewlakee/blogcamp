package ru.aleynikov.blogcamp.domain.modelMappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.domain.models.Post;
import ru.aleynikov.blogcamp.services.TagService;
import ru.aleynikov.blogcamp.services.UserService;

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
        post.setDeleted(rs.getBoolean("deleted"));
        post.setCommentCount(rs.getInt("comments_count"));
        post.setTags(tagService.getTagsListByPostId(post.getId()));
        post.setUser(userService.findUserById(rs.getInt("user")));

        return post;
    }
}
