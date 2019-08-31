package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Post {

    private int id;
    private User user;
    private String label;
    private String text;
    private List<Tag> tags;
}
