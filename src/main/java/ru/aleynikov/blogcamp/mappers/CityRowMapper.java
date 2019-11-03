package ru.aleynikov.blogcamp.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.models.City;
import ru.aleynikov.blogcamp.services.CountryService;

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
