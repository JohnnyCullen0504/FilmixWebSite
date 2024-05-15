package com.hhu.filmix.repository;

import com.hhu.filmix.api.Page;
import com.hhu.filmix.entity.Movie;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovieRepository {

    Optional<Movie> findMovieById(Long id);

    public Long insertMovie(Movie movie);
    public void updateMovie(Movie movie);
    public void deleteMovie(Long id);
    public Page<Movie> findMovieByName(String name, Integer pageSize, Integer currentPage);

    List<Movie> getNowShowingMovie(LocalDateTime today);

    List<Movie> getUpComingMovie(LocalDateTime now);

    Page<Movie> findALlMovies(Integer currentPage, Integer pageSize);

    List<Movie> findALlMovies();

    Page<Movie> queryMovies(Integer currentPage, Integer pageSize, String name);

    Page<Movie> findDeletedMovies(Integer currentPage, Integer pageSize);

    Optional<Movie> findDeletedMovieById(Long id);

    void undeleteMovie(Long id);

    void permanentDeleteMovie(Long id);

    List<Movie> getNowShowingMovieByCinema(Long cinemaId);

    List<Movie> findMovieByNameEqual(String name);
}
