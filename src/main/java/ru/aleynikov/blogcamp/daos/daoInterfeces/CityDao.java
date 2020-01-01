package ru.aleynikov.blogcamp.daos.daoInterfeces;

import ru.aleynikov.blogcamp.domain.models.City;

import java.util.List;
import java.util.Optional;

public interface CityDao {

   List<City> queryForList(String query, Object[] qparams);
   Optional<City> queryForObject(String query, Object[] qparams);
}
