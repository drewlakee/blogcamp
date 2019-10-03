package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Setter
@Getter
public class Post {

    private int id;
    private int user;
    private String title;
    private String text;
    private String introImage;
    private Date createdDate;
    private boolean isBanned;
}
