package com.hhu.filmix.repository.impl;

import com.hhu.filmix.api.Page;
import com.hhu.filmix.dtoMapper.MovieDTOMapper;
import com.hhu.filmix.entity.Movie;
import com.hhu.filmix.repository.MovieRepository;
import com.hhu.filmix.service.ObjectStorageService;
import com.hhu.filmix.util.PagingTool;
import com.hhu.filmix.util.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

@Repository
public class MovieRepositoryImpl implements MovieRepository {
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Movie> movieRowMapper;
    private SnowFlake snowFlake;
    private MovieDTOMapper movieDTOMapper;
    private ObjectStorageService objectStorageService;
    @Override
    public Optional<Movie> findMovieById(Long id) {
        var sql = """
                SELECT * FROM movie WHERE movie_id =? AND movie_deleted = false;
                """;
        return jdbcTemplate.query(sql,movieRowMapper,id).stream().findFirst();
    }

    @Override
    public Long insertMovie(Movie movie) {
        movie.setId(snowFlake.genId());
        var sql = """
                INSERT INTO movie(movie_id,movie_name,duration,release_time,poster,language,source,movie_deleted)
                VALUES (?,?,?,?,?,?,?,?);
                """;
        Object[] args ={
                movie.getId(),
                movie.getName(),
                movie.getDuration(),
                movie.getReleaseTime(),
                movie.getPosterURL(),
                movie.getLanguage().getCode(),
                movie.getSource(),
                movie.getDeleted()
        };
        jdbcTemplate.update(sql,args);
        return movie.getId();
    }

    @Override
    public void updateMovie(Movie movie) {
        var sql = """
                UPDATE movie SET movie_name=?, duration=?, release_time=?,poster=?,language=?,source=? WHERE movie_id=?;
                """;
        Object[] args ={
                movie.getName(),
                movie.getDuration(),
                movie.getReleaseTime(),
                movie.getPosterURL(),
                movie.getLanguage().getCode(),
                movie.getSource(),
                movie.getId()
        };
        jdbcTemplate.update(sql,args);
    }

    @Override
    public void deleteMovie(Long id) {
        var sql = """
                UPDATE movie SET movie_deleted = true WHERE movie_id = ?;
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public Page<Movie> findMovieByName(String name, Integer pageSize, Integer currentPage) {
        Integer offset = PagingTool.offset(pageSize,currentPage);
        var sql= """
                SELECT * FROM movie WHERE mvoie_name LIKE CONCAT('%',?,'%') AND movie_deleted = false
                """;
        var countSql = "SELECT COUNT(*) FROM (" + sql+  ") AS t* ";

        var pagingSql = "SELECT * FROM (" + sql+  ") AS t* LIMIT ?,?";

        Integer count = jdbcTemplate.query(countSql,rs -> {return rs.getInt("count");},name);
        List<Movie> movies = jdbcTemplate.query(pagingSql,movieRowMapper,name,offset,pageSize);
        Integer totalPage = PagingTool.totalPage(count,pageSize);

        return Page.<Movie>builder()
                .currentPage(currentPage)
                .totalPage(totalPage)
                .pageSize(pageSize)
                .records(movies)
                .build();

    }

    @Override
    public List<Movie> getNowShowingMovie(LocalDateTime today) {
//        var sql = """
//        SELECT
//            M.movie_id AS 'movie_id',
//            ANY_VALUE(M.movie_name) AS 'movie_name',
//            ANY_VALUE(M.duration) AS 'duration',
//            ANY_VALUE(M.release_time) AS 'release_time',
//            ANY_VALUE(M.poster) AS 'poster',
//            ANY_VALUE(M.language) AS 'language',
//            ANY_VALUE(M.source) AS 'source',
//            ANY_VALUE(M.movie_deleted) AS 'movie_deleted'
//
//        FROM movie M
//        WHERE M.movie_id IN (
//            SELECT movie_id FROM ticket
//            WHERE show_time >= NOW() AND canceled = false
//            GROUP BY movie_id)
//        AND  M.movie_deleted = false;
//                """;
            var view_sql = """
                    SELECT * FROM now_showing_movie
                    """;
        return jdbcTemplate.query(view_sql,movieRowMapper);
    }

    @Override
    public List<Movie> getUpComingMovie(LocalDateTime now) {
        var sql = """
                SELECT * FROM movie WHERE release_time > NOW() AND movie_deleted = false
                """;
        return jdbcTemplate.query(sql,movieRowMapper);
    }

    @Override
    public Page<Movie> findALlMovies(Integer currentPage, Integer pageSize) {
        Integer offset = PagingTool.offset(pageSize,currentPage);
        var querySql = "SELECT * FROM movie WHERE movie_deleted = FALSE";
        var countSql = "SELECT COUNT(*) AS c FROM ("+querySql +") AS t";
        var pagingSql = "SELECT * FROM ("+ querySql+") AS t LIMIT ?,?";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
        List<Movie> movies = jdbcTemplate.query(pagingSql,movieRowMapper,offset,pageSize);
        return Page.<Movie>builder()
                .records(movies)
                .currentPage(currentPage)
                .totalPage(PagingTool.totalPage(count,pageSize))
                .build();
    }
    @Override
    public List<Movie> findALlMovies(){
        var sql = """
                SELECT * FROM movie WHERE movie_deleted = FALSE
                """;
        return jdbcTemplate.query(sql,movieRowMapper);
    }
    public Page<Movie> queryMovies(Integer currentPage, Integer pageSize, String name) {
        Integer offset =PagingTool.offset(pageSize,currentPage);
        var querySql = "SELECT * FROM movie WHERE movie_name LIKE CONCAT('%',?,'%') AND movie_deleted = FALSE";
        var countSql = "SELECT COUNT(*) AS c FROM ("+querySql +") AS t";
        var pagingSql = "SELECT * FROM ("+ querySql+") AS t LIMIT ?,?";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class,name);
        List<Movie> movies = jdbcTemplate.query(pagingSql,movieRowMapper,name,offset,pageSize);
        return Page.<Movie>builder()
                .records(movies)
                .currentPage(currentPage)
                .totalPage(PagingTool.totalPage(count,pageSize))
                .build();
    }

    @Override
    public Page<Movie> findDeletedMovies(Integer currentPage, Integer pageSize) {
        Integer offset = PagingTool.offset(pageSize,currentPage);
        var querySql = "SELECT * FROM movie WHERE movie_deleted = TRUE";
        var countSql = "SELECT COUNT(*) AS c FROM ("+querySql +") AS t";
        var pagingSql = "SELECT * FROM ("+ querySql+") AS t LIMIT ?,?";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
        List<Movie> movies = jdbcTemplate.query(pagingSql,movieRowMapper,offset,pageSize);
        return Page.<Movie>builder()
                .records(movies)
                .currentPage(currentPage)
                .totalPage(PagingTool.totalPage(count,pageSize))
                .build();
    }

    @Override
    public Optional<Movie> findDeletedMovieById(Long id) {
        var sql = """
                SELECT * FROM movie WHERE movie_deleted = TRUE AND movie_id = ?
                """;
        return jdbcTemplate.query(sql,movieRowMapper,id).stream().findFirst();
    }

    @Override
    public void undeleteMovie(Long id) {
        var sql = """
                UPDATE movie SET movie_deleted = FALSE WHERE  movie_id = ?
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public void permanentDeleteMovie(Long id) {
        var sql = """
                DELETE FROM movie WHERE movie_id = ?
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public List<Movie> getNowShowingMovieByCinema(Long cinemaId) {
        var sql = """
        SELECT
            M.movie_id AS 'movie_id',
            ANY_VALUE(M.movie_name) AS 'movie_name',
            ANY_VALUE(M.duration) AS 'duration',
            ANY_VALUE(M.release_time) AS 'release_time',
            ANY_VALUE(M.poster) AS 'poster',
            ANY_VALUE(M.language) AS 'language',
            ANY_VALUE(M.source) AS 'source',
            ANY_VALUE(M.movie_deleted) AS 'movie_deleted'

        FROM movie M
        WHERE M.movie_id IN (
            SELECT movie_id FROM ticket
            WHERE show_time >= NOW() AND canceled = false AND cinema_id = ?
            GROUP BY movie_id)
        AND  M.movie_deleted = false;
                """;
        return jdbcTemplate.query(sql,movieRowMapper,cinemaId);
    }

    @Override
    public List<Movie> findMovieByNameEqual(String name) {
        var sql = """
                SELECT * FROM movie WHERE movie_name = ? AND movie_deleted = false
                """;
        return jdbcTemplate.query(sql,movieRowMapper,name);
    }

    public MovieRepositoryImpl(JdbcTemplate jdbcTemplate,
                               RowMapper<Movie> movieRowMapper,
                               SnowFlake snowFlake,
                               MovieDTOMapper movieDTOMapper,
                               ObjectStorageService objectStorageService) {
        this.jdbcTemplate = jdbcTemplate;
        this.movieRowMapper = movieRowMapper;
        this.snowFlake = snowFlake;
        this.movieDTOMapper = movieDTOMapper;
        this.objectStorageService = objectStorageService;
    }
}
