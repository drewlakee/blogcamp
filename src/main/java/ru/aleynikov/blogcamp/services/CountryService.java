package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daoImpls.CountryDaoImpl;
import ru.aleynikov.blogcamp.models.Country;

import java.util.List;

@Service
public class CountryService {

    @Autowired
    private CountryDaoImpl countryDao;

    public List<Country> getAllCountriesList() {
        return countryDao.findAllCountries();
    }

    public Country findById(int id) {
        return countryDao.findById(id);
    }
}
