package ru.aleynikov.blogcamp.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.models.Comment;
import ru.aleynikov.blogcamp.services.PostService;
import ru.aleynikov.blogcamp.services.UserService;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CommentRowMapper implements RowMapper {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getInt("comment_id"));
        comment.setText(rs.getString("text"));
        comment.setCreatedDate(rs.getTimestamp("created_date"));
        comment.setUser(userService.findById(rs.getInt("user")));
        comment.setPost(postService.findById(rs.getInt("post")));

        return comment;
    }
}
