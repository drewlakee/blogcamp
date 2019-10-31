package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpls.CityDaoImpl;
import ru.aleynikov.blogcamp.models.City;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityDaoImpl cityDao;

    public List<City> findAllCities() {
        String query = "SELECT * FROM city";

        return cityDao.queryForList(query, null);
    }

    public City findCityById(int id) {
        String query = "SELECT * FROM city WHERE city_id = ?";
        Object[] qparams = new Object[] {id};

        return cityDao.queryForObject(query, qparams);
    }

}
