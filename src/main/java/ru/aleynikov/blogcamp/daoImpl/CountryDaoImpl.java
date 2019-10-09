package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.CountryDao;
import ru.aleynikov.blogcamp.mapper.CountryRowMapper;
import ru.aleynikov.blogcamp.model.Country;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.List;

@Component
public class CountryDaoImpl implements CountryDao {

    private static final Logger log = LoggerFactory.getLogger(CountryDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CountryRowMapper countryRowMapper;

    @Override
    public List<Country> allCountriesList() {
        String query = "SELECT * FROM country";
        List<Country> countryList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        countryList = jdbc.query(query, countryRowMapper);

        return countryList;
    }
}
