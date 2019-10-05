package ru.aleynikov.blogcamp.model;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Setter
@Getter
public class Tag {

    private int id;
    private String name;
    private String description;
    private int postCount;
    private Timestamp createdDate;

    public Tag() {}
}
