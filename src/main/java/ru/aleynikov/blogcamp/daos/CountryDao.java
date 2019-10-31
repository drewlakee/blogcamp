package ru.aleynikov.blogcamp.daos;

import ru.aleynikov.blogcamp.models.Country;

import java.util.List;

public interface CountryDao {

    List<Country> findAllCountries();
    Country findById(int id);
}
