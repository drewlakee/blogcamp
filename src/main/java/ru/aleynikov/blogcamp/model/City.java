package ru.aleynikov.blogcamp.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {

    int id;
    String cityName;
    String countryName;

    public City() {}
}
