package ru.aleynikov.blogcamp.daos.extensions;

import java.util.List;
import java.util.Optional;

public interface StandartQueryable {

    List queryForList(String query, Object[] qparams);
    Optional queryForObject(String query, Object[] qparams);
}
