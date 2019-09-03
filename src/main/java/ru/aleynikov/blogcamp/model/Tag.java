package ru.aleynikov.blogcamp.model;


import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class Tag {

    private int id;
    private String name;
    private String description;
    private int postCount;
    private Date createdDate;

    public Tag(int id, String name, String description, int postCount, Date createdDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.postCount = postCount;
        this.createdDate = createdDate;
    }

    public Tag() {}
}
