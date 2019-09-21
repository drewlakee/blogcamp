package ru.aleynikov.blogcamp.rowMapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.aleynikov.blogcamp.model.Country;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CountryRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Country country = new Country();
        country.setId(rs.getInt("country_id"));
        country.setCountryName(rs.getString("name"));

        return country;
    }
}
