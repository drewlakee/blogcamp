package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class Comment {

    private int id;
    private String text;
    private Timestamp createdDate;
    private User user;
    private Post post;

    public Comment(String text, Timestamp createdDate, User user) {
        this.text = text;
        this.createdDate = createdDate;
        this.user = user;
    }

    public Comment() {}
}
