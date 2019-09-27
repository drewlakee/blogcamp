package ru.aleynikov.blogcamp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpl.CountryDaoImpl;
import ru.aleynikov.blogcamp.model.Country;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryDaoImpl countryDao;

    public List<Country> getAllCountriesList() {
        return countryDao.getAllCountriesList();
    }
}
