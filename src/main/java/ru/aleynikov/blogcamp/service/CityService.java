package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.CityDaoImpl;
import ru.aleynikov.blogcamp.model.City;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityDaoImpl cityDao;

    public List<City> getAllCities() {
        return cityDao.allCitiesList();
    }

}
