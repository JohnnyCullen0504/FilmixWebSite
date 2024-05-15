package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.MovieTag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class MovieTagRowMapper implements RowMapper<MovieTag> {
    @Override
    public MovieTag mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MovieTag(
                rs.getLong("tag_id"),
                rs.getString("tag_name"),
                rs.getString("tag_name_ZH"));
    }
}
