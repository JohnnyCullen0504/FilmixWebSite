package com.hhu.filmix.repository.impl;

import com.hhu.filmix.entity.MovieTag;
import com.hhu.filmix.repository.MovieTagRepository;
import com.hhu.filmix.util.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class MovieTageRepositoryImpl implements MovieTagRepository {
    private RowMapper<MovieTag> movieTagRowMapper;
    private JdbcTemplate jdbcTemplate;
    private SnowFlake snowFlake;
    @Override
    public Optional<MovieTag> findTagById(Long id) {
        var sql = """
                SELECT * FROM tag WHERE tag_id =?;
                """;
        return jdbcTemplate.query(sql,movieTagRowMapper,id).stream().findFirst();

    }

    @Override
    public Long insertTag(MovieTag tag) {
        tag.setId(snowFlake.genId());
        var sql = """
                INSERT INTO tag(tag_id,tag_name,tag_name_ZH) VALUES (?,?,?);
                """;
        Object[] args ={
                tag.getId(),
                tag.getName(),
                tag.getNameZH()
        };
        jdbcTemplate.update(sql,args);
        return tag.getId();
    }

    @Override
    public Long insertMovieAndTagRelationShip(Long movieId, Long tagId) {
        var sql = """
                INSERT INTO tag_include(movie_id,tag_id) VALUES (?,?);
                """;
        Object[] args={
                movieId,
                tagId
        };
        jdbcTemplate.update(sql,args);
        return null;
    }

    @Override
    public void clearRelationShipForMovie(Long movieId) {
        var sql = """
                DELETE FROM tag_include WHERE movie_id = ?;
                """;
        jdbcTemplate.update(sql,movieId);
    }

    @Override
    public void clearRelationShipForTag(Long tagId) {
        var sql = """
                DELETE FROM tag_include WHERE tag_id = ?;
                """;
        jdbcTemplate.update(sql,tagId);
    }


    @Override
    public void deleteTag(Long tagId) {
        var sql = """
                DELETE FROM tag WHERE tag_id = ?;
                """;
        jdbcTemplate.update(sql,tagId);
    }

    @Override
    public List<MovieTag> getAllTages() {
        var sql = """
                SELECT * FROM tag;
                """;
        return jdbcTemplate.query(sql,movieTagRowMapper);
    }

    @Override
    public List<MovieTag> findMovieTags(Long id) {
        var sql = """
                SELECT T.tag_id AS tag_id, T.tag_name AS tag_name, T.tag_name_ZH as tag_name_ZH FROM tag_include TI
                LEFT JOIN tag T ON TI.tag_id = T.tag_id
                WHERE TI.movie_id = ?;
                """;
        return jdbcTemplate.query(sql,movieTagRowMapper,id);
    }

    public MovieTageRepositoryImpl(RowMapper<MovieTag> movieTagRowMapper,
                                   JdbcTemplate jdbcTemplate,
                                   SnowFlake snowFlake) {
        this.movieTagRowMapper = movieTagRowMapper;
        this.jdbcTemplate = jdbcTemplate;
        this.snowFlake = snowFlake;
    }
}
