package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.City;

import java.util.List;

public interface CityDao {

   List<City> queryList(String query, Object[] qparams);
   City queryForObject(String query, Object[] qparams);
}
