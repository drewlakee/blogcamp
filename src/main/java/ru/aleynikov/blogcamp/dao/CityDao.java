package ru.aleynikov.blogcamp.dao;

import ru.aleynikov.blogcamp.model.City;

public interface CityDao {

    City findCityByName(String cityName);
    City findCityById(int id);
}
