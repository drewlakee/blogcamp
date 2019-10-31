package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.Country;

import java.util.List;

public interface CountryDao {

    List<Country> queryForList(String query, Object[] qparams);
    Country queryForObject(String query, Object[] qparams);
}
