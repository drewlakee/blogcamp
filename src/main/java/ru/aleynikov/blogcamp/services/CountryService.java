package ru.aleynikov.blogcamp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.aleynikov.blogcamp.daos.daoInterfeces.CountryDao;
import ru.aleynikov.blogcamp.domain.models.Country;

import java.util.List;
import java.util.Optional;

@Service
public class CountryService {

    @Autowired
    private CountryDao countryDao;

    public List<Country> getAllCountriesList() {
        String query = "SELECT * FROM country";

        return countryDao.queryForList(query, null);
    }

    public Optional<Country> findById(int id) {
        String query = "SELECT * FROM country WHERE country_id = ?";
        Object[] qparams = new Object[] {id};

        return countryDao.queryForObject(query, qparams);
    }
}
