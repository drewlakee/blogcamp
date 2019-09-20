package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class User {

    private int id;
    private String username;
    private String fullName;
    private String password;
    private String secretQuestion;
    private String secretAnswer;
    private boolean active;
    private Date registeredDate;
    private String about;
    private Date birthday;
    private String country;
    private String city;
    private String authority;

    public User() {}
}
