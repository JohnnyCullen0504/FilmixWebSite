package com.hhu.filmix.service;

import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.cinemaDTO.request.CinemaEditRequest;
import com.hhu.filmix.dto.cinemaDTO.request.CinemaRegisterRequest;
import com.hhu.filmix.dto.cinemaDTO.response.CinemaDTO;
import com.hhu.filmix.entity.Cinema;

public interface CinemaService {
    public ApiResult<CinemaDTO> getCinemaInfo(Long CinemaId);
    public ApiResult<?> registerCinema(CinemaRegisterRequest request);
    public ApiResult<?> deleteCinema(Long CinemaId);
    public ApiResult<?> editCinema(CinemaEditRequest request);

    ApiResult<Page<CinemaDTO>> getRooms(Integer currentPage, Integer pageSize,String name);

    ApiResult<Page<Cinema>> getDeletedCinemas(Integer currentPage, Integer pageSize);

    ApiResult<?> undeletedCinemas(Long id);

    ApiResult<?> permanentDeleteCinema(Long id);
}
