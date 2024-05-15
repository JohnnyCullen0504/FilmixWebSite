package com.hhu.filmix.service;

import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.MovieTagDTO.response.MovieTagDTO;

import java.util.List;

public interface TagService {
    ApiResult<?> newMovieTag(String name, String nameZH);

    ApiResult<?> deleteMovieTag(Long id);

    ApiResult<List<MovieTagDTO>> getAllTags();
}
