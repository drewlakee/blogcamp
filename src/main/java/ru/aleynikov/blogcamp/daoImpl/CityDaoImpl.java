package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.daos.CityDao;
import ru.aleynikov.blogcamp.mappers.CityRowMapper;
import ru.aleynikov.blogcamp.models.City;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.Arrays;
import java.util.List;

@Component
public class CityDaoImpl implements CityDao {

    private static final Logger log = LoggerFactory.getLogger(CityDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CityRowMapper cityRowMapper;

    @Override
    public List<City> findAllCities() {
        String query = "SELECT * FROM city";
        List<City> cityList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        cityList = jdbc.query(query, cityRowMapper);

        return cityList;
    }

    @Override
    public City findCityById(int id) {
        String query = "SELECT * FROM city WHERE city_id = ?";
        Object[] qparams = new Object[] { id };
        City city = null;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            city = (City) jdbc.queryForObject(query, qparams, cityRowMapper);
        } catch (EmptyResultDataAccessException e) {}

        return city;
    }
}
