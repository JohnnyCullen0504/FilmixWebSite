package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.Cinema;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class CinemaRowMapper implements RowMapper<Cinema> {

    @Override
    public Cinema mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cinema cinema = new Cinema(
                rs.getLong("cinema_id"),
                rs.getString("cinema_name"),
                rs.getString("address"),
                rs.getBoolean("cinema_deprecated")
        );
        return cinema;
    }

}
