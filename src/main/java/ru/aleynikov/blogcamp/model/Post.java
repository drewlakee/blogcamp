package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
public class Post {

    private int id;
    private User user;
    private String title;
    private String text;
    private String introImage;
    private Timestamp createdDate;
    private boolean isBanned;
    private List<Tag> tags;
}
