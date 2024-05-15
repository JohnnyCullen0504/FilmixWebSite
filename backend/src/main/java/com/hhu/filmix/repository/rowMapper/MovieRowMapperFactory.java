package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.Movie;
import com.hhu.filmix.enumeration.Language;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDate;

@Configuration
public class MovieRowMapperFactory {
    @Bean
    public RowMapper<Movie> movieRowMapper(){
        return (rs, rowNum) -> {
          Movie movie = new Movie(
                  rs.getLong("movie_id"),
                  rs.getString("movie_name"),
                  rs.getInt("duration"),
                  rs.getObject("release_time", LocalDate.class),
                  rs.getString("poster"),
                  Language.Language(rs.getString("language")),
                  rs.getString("source"),
                  rs.getBoolean("movie_deleted"));
          return movie;
        };
    }
}
