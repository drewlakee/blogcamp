package ru.aleynikov.blogcamp.models;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Setter
@Getter
public class Tag {

    private int id;
    private String name;
    private String description;
    private Timestamp createdDate;

    public Tag() {}
}
