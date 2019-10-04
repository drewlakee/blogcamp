package ru.aleynikov.blogcamp.daoImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.dao.CityDao;
import ru.aleynikov.blogcamp.model.City;
import ru.aleynikov.blogcamp.mapper.CityRowMapper;
import ru.aleynikov.blogcamp.security.SecurityUtils;

import java.util.List;

@Component
public class CityDaoImpl implements CityDao {

    private static final Logger log = LoggerFactory.getLogger(CityDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CityRowMapper cityRowMapper;

    @Override
    public List<City> allCitiesList() {
        String query = "SELECT city_id, city.name as \"city_name\", country.name as \"country_name\" FROM  city, country WHERE city.country = country.country_id;";
        List<City> cityList;

        log.info(SecurityUtils.getPrincipal().getUsername() + ": " + query);
        cityList = jdbc.query(query, cityRowMapper);

        return cityList;
    }
}
