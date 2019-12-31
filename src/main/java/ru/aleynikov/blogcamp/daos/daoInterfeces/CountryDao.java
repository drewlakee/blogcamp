package ru.aleynikov.blogcamp.daos.daoInterfeces;

import ru.aleynikov.blogcamp.domain.models.Country;

import java.util.List;
import java.util.Optional;

public interface CountryDao {

    List<Country> queryForList(String query, Object[] qparams);
    Optional<Country> queryForObject(String query, Object[] qparams);
}
