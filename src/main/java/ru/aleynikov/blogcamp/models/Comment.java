package ru.aleynikov.blogcamp.models;

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
}
