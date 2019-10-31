package ru.aleynikov.blogcamp.models;

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
    private boolean isDeleted;
    private List<Tag> tags;
    private int commentCount;
}
