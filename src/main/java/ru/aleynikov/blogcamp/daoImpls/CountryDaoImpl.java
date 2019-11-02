package ru.aleynikov.blogcamp.daoImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.daos.CountryDao;
import ru.aleynikov.blogcamp.mappers.CountryRowMapper;
import ru.aleynikov.blogcamp.models.City;
import ru.aleynikov.blogcamp.models.Country;
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
    public List<Country> queryForList(String query, Object[] qparams) {
        List<Country> countries;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
        countries = jdbc.query(query, qparams, countryRowMapper);

        return countries;
    }

    @Override
    public Country queryForObject(String query, Object[] qparams) {
        Country country;

        try {
            log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query + ", {}", Arrays.toString(qparams));
            country = (Country) jdbc.queryForObject(query, qparams, countryRowMapper);
        } catch (EmptyResultDataAccessException e) {
            country = null;
        }

        return country;
    }
}
