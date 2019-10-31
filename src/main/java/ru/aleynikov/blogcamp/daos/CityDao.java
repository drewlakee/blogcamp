package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.City;

import java.util.List;

public interface CityDao {

   List<City> findAllCities();
   City findCityById(int id);
}
