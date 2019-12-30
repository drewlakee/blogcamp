package ru.aleynikov.blogcamp.daos.daoImpls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.daos.daoInterfeces.CityDao;
import ru.aleynikov.blogcamp.domain.modelMappers.CityRowMapper;
import ru.aleynikov.blogcamp.domain.models.City;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CityDaoImpl implements CityDao {

    private static final Logger log = LoggerFactory.getLogger(CityDaoImpl.class);

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CityRowMapper cityRowMapper;

    @Override
    public List<City> queryForList(String query, Object[] qparams) {
        List<City> cities;
        log.info(query + ", {}", Arrays.toString(qparams));
        cities = jdbc.query(query, qparams, cityRowMapper);

        return cities;
    }

    @Override
    public Optional<City> queryForObject(String query, Object[] qparams) {
        Optional<City> city;

        try {
            log.info(query + ", {}", Arrays.toString(qparams));
            city = Optional.of((City) jdbc.queryForObject(query, qparams, cityRowMapper));
        } catch (EmptyResultDataAccessException e) {
            city = Optional.empty();
        }

        return city;
    }
}
