package ru.aleynikov.blogcamp.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.model.City;
import ru.aleynikov.blogcamp.service.CountryService;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CityRowMapper implements RowMapper {

    @Autowired
    private CountryService countryService;

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        City city = new City();
        city.setId(rs.getInt("city_id"));
        city.setName(rs.getString("name"));
        city.setCountry(countryService.findById(rs.getInt("country")));

        return city;
    }
}
