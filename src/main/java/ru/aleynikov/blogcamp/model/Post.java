package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
public class Post {

    private int id;
    private User user;
    private String title;
    private String text;
    private String introImage;
    private Date createdDate;
    private boolean isBanned;
    private List<Tag> tags;
}
