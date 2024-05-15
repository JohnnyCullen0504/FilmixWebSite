package com.hhu.filmix.service;

import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.roomDTO.RoomDTO;
import com.hhu.filmix.dto.roomDTO.request.EditRoomRequest;
import com.hhu.filmix.dto.roomDTO.request.RegisterRoomRequest;
import com.hhu.filmix.entity.Room;

import java.util.List;

public interface RoomService {
    ApiResult<?> registerRoom(RegisterRoomRequest request);

    ApiResult<List<RoomDTO>> getAllRoomByCinema(Long cinemaId);

    ApiResult<?> deleteRoom(Long id);

    ApiResult<Page<Room>> getDeletedMovies(Integer currentPage, Integer pageSize);

    ApiResult<?> undeletedMovies(Long id);

    ApiResult<?> permanentDeleteMovie(Long id);

    ApiResult<?> editRoom(EditRoomRequest request);
}
