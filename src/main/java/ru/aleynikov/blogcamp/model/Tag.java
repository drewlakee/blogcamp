package ru.aleynikov.blogcamp.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Tag {

    private int id;
    private String name;
    private String description;
    private int postCount;

    public Tag(int id, String name, String description, int postCount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.postCount = postCount;
    }

    public Tag() {
    }
}
