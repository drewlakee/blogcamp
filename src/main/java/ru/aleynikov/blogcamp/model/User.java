package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;

@Setter
@Getter
public class User {

    private int id;
    private String username;
    private String password;
    private LinkedList<Post> posts;

}
