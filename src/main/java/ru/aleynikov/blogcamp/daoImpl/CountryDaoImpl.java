package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.CountryDao;
import ru.aleynikov.blogcamp.mapper.CountryRowMapper;
import ru.aleynikov.blogcamp.model.Country;
import ru.aleynikov.blogcamp.model.User;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class CountryDaoImpl implements CountryDao {

    private static final Logger log = LoggerFactory.getLogger(CountryDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CountryRowMapper countryRowMapper;

    @Override
    public List<Country> findAllCountries() {
        String query = "SELECT * FROM country";
        List<Country> countryList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        countryList = jdbc.query(query, countryRowMapper);

        return countryList;
    }

    @Override
    public Country findById(int id) {
        String query = "SELECT * FROM country WHERE country_id = ?";
        Object[] qparams = new Object[] {id};
        Country country = null;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            country = (Country) jdbc.queryForObject(query, qparams, countryRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return country;
    }
}
