package com.hhu.filmix.service;

import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.movieDTO.response.MovieBriefInfo;
import com.hhu.filmix.dto.movieDTO.response.MovieDTO;
import com.hhu.filmix.entity.Movie;
import com.hhu.filmix.enumeration.Language;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    ApiResult<MovieDTO> getMovieInfo(Long movieId);

    ApiResult<?> createNewMovie(String name, Integer duration, LocalDate release, List<Long> category, Language language, String source, MultipartFile posterFile);

    ApiResult<?> editMovie(Long id, String name, Integer duration, LocalDate release, List<Long> category, Language language, String source, MultipartFile posterFile);

    ApiResult<?> deleteMovie(Long movieId);

    ApiResult<Page<MovieBriefInfo>> getAllMovies(Integer currentPage, Integer pageSize);

    ApiResult<Page<MovieBriefInfo>> queryMovies(Integer currentPage, Integer pageSize,String name);

    ApiResult<Page<Movie>> getDeletedMovies(Integer currentPage, Integer pageSize);

    ApiResult<?> undeletedMovies(Long id);

    ApiResult<?> permanentDeleteMovie(Long id);

    ApiResult<List<MovieDTO>> getAllMoviesDetails();
}
