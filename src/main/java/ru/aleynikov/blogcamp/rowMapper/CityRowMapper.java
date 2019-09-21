package ru.aleynikov.blogcamp.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.model.City;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CityRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        City city = new City();
        city.setId(rs.getInt("city_id"));
        city.setCityName(rs.getString("city_name"));
        city.setCountryName(rs.getString("country_name"));

        return city;
    }
}
