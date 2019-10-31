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
        String query = "SELECT * FROM country";

        return countryDao.queryList(query, null);
    }

    public Country findById(int id) {
        String query = "SELECT * FROM country WHERE country_id = ?";
        Object[] qparams = new Object[] {id};

        return countryDao.queryForObject(query, qparams);
    }
}
