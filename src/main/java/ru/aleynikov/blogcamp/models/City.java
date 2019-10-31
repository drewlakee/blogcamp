package ru.aleynikov.blogcamp.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    int id;
    String name;
    Country country;
}
