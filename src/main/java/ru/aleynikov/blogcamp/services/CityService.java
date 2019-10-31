package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.CityDaoImpl;
import ru.aleynikov.blogcamp.models.City;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityDaoImpl cityDao;

    public List<City> findAllCities() {
        return cityDao.findAllCities();
    }
    public City findCityById(int id) { return cityDao.findCityById(id); }

}
