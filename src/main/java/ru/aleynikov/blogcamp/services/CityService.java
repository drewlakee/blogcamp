package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daos.daoInterfeces.CityDao;
import ru.aleynikov.blogcamp.domain.models.City;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    private CityDao cityDao;

    public List<City> getAllCitiesList() {
        String query = "SELECT * FROM city";

        return cityDao.queryForList(query, null);
    }

    public Optional<City> findCityById(int id) {
        String query = "SELECT * FROM city WHERE city_id = ?";
        Object[] qparams = new Object[] {id};

        return cityDao.queryForObject(query, qparams);
    }

}
