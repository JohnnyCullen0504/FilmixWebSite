package com.hhu.filmix.repository.impl;

import com.hhu.filmix.api.Page;
import com.hhu.filmix.entity.Cinema;
import com.hhu.filmix.entity.User;
import com.hhu.filmix.repository.CinemaRepository;
import com.hhu.filmix.util.PagingTool;
import com.hhu.filmix.util.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
@Repository
public class CinemaRepositoryImpl implements CinemaRepository {
    private  SnowFlake snowFlake;
    private  JdbcTemplate jdbcTemplate;
    private  RowMapper<Cinema> cinemaRowMapper;
    private  RowMapper<User> userRowMapper;

    @Override
    public Optional<Cinema> findCinemaById(Long id) {
        String sql = """
                SELECT * FROM cinema WHERE cinema_id = ? AND cinema_deprecated = false;
                """;
        return jdbcTemplate.query(sql, cinemaRowMapper,id).stream().findFirst();
    }

    @Override
    public Long insertCinema(Cinema cinema) {
        cinema.setId(snowFlake.genId());
        String sql= """
                INSERT INTO cinema(cinema_id,cinema_name,address,cinema_deprecated) VALUE (?,?,?,?);
                """;
        jdbcTemplate.update(sql,
                cinema.getId(),
                cinema.getName(),
                cinema.getAddress(),
                cinema.getDeprecated());
        return cinema.getId();
    }

    @Override
    public void updateCinema(Cinema cinema) {
        String sql = """
                UPDATE cinema SET cinema_name = ?, address = ? WHERE cinema_id = ?;
                """;
        jdbcTemplate.update(sql,
                cinema.getName(),
                cinema.getAddress(),
                cinema.getId());
    }

    @Override
    public void deleteCinemaById(Long id) {
        String sql = """
                DELETE FROM cinema WHERE cinema_id = ?;
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public void deprecatedCinema(Long id) {
        var sql = """
                UPDATE cinema SET cinema_deprecated = true WHERE cinema_id = ?
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public Page<Cinema> findAllCinemas(Integer currentPage, Integer pageSize) {
        var querySQL = """
                SELECT * FROM cinema WHERE cinema_deprecated = false
                """;
        var pageSQL = "SELECT * FROM ("+querySQL+") AS t LIMIT ?,?";
        var countSQL = "SELECT COUNT(*) FROM ("+ querySQL +") AS t";

        Integer offset = PagingTool.offset(pageSize,currentPage);
        Integer count = jdbcTemplate.queryForObject(countSQL, Integer.class);

        List<Cinema> cinemas = jdbcTemplate.query(pageSQL,cinemaRowMapper,offset,pageSize);
        return Page.<Cinema>builder()
                .records(cinemas)
                .totalPage(PagingTool.totalPage(count,pageSize))
                .pageSize(pageSize)
                .currentPage(currentPage)
                .build();
    }

    @Override
    public Page<Cinema> findCinemasByName(Integer currentPage, Integer pageSize, String name) {
        var querySQL = """
                SELECT * FROM cinema WHERE cinema_name LIKE CONCAT('%',?,'%') AND cinema_deprecated = false
                """;
        var pageSQL = "SELECT * FROM ("+querySQL+") as t LIMIT ?,?";
        var countSQL = "SELECT COUNT(*) FROM ("+ querySQL +") as t";

        Integer offset = PagingTool.offset(pageSize,currentPage);
        Integer count = jdbcTemplate.queryForObject(countSQL, Integer.class,name);

        List<Cinema> cinemas = jdbcTemplate.query(pageSQL,cinemaRowMapper,name,offset,pageSize);
        return Page.<Cinema>builder()
                .records(cinemas)
                .totalPage(PagingTool.totalPage(count,pageSize))
                .pageSize(pageSize)
                .currentPage(currentPage)
                .build();
    }

    @Override
    public Page<Cinema> findDeletedCinema(Integer currentPage, Integer pageSize) {
        Integer offset = PagingTool.offset(pageSize,currentPage);
        var querySql = "SELECT * FROM cinema WHERE cinema_deprecated = TRUE";
        var countSql = "SELECT COUNT(*) AS c FROM ("+querySql +") AS t";
        var pagingSql = "SELECT * FROM ("+ querySql+") AS t LIMIT ?,?";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
        List<Cinema> cinemas = jdbcTemplate.query(pagingSql,cinemaRowMapper,offset,pageSize);
        return Page.<Cinema>builder()
                .records(cinemas)
                .currentPage(currentPage)
                .totalPage(PagingTool.totalPage(count,pageSize))
                .build();
    }

    @Override
    public Optional<Cinema> findDeletedCinemaById(Long id) {
        var sql = """
                SELECT * FROM cinema WHERE cinema_deprecated = TRUE AND cinema_id = ?
                """;
        return jdbcTemplate.query(sql,cinemaRowMapper,id).stream().findFirst();
    }

    @Override
    public void undeleteCinema(Long id) {
        var sql = """
                UPDATE cinema SET cinema_deprecated = FALSE WHERE  cinema_id = ?
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public void permanentDeleteCinema(Long id) {
        var sql = """
                DELETE FROM cinema WHERE cinema_id = ? 
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public List<Cinema> findByNameEqual(String name) {
        var sql = """
                SELECT * FROM cinema WHERE cinema_name = ? AND cinema_deprecated = FALSE
                """;
        return jdbcTemplate.query(sql,cinemaRowMapper,name);
    }

    @Override
    public List<Cinema> findByAddressEqual(String address) {
        var sql = """
                SELECT * FROM cinema WHERE address = ? AND cinema_deprecated = FALSE
                """;
        return jdbcTemplate.query(sql,cinemaRowMapper,address);
    }


    public CinemaRepositoryImpl(SnowFlake snowFlake,
                                JdbcTemplate jdbcTemplate,
                                RowMapper<Cinema> CinemaRowmapper,
                                RowMapper<User> userRowMapper) {
        this.snowFlake = snowFlake;
        this.jdbcTemplate = jdbcTemplate;
        this.cinemaRowMapper = CinemaRowmapper;
        this.userRowMapper = userRowMapper;
    }
}
