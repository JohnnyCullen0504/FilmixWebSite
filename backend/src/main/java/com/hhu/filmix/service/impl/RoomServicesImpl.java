package com.hhu.filmix.service.impl;

import com.hhu.filmix.api.ApiCode;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.PutResponseDTO;
import com.hhu.filmix.dto.roomDTO.RoomDTO;
import com.hhu.filmix.dto.roomDTO.request.EditRoomRequest;
import com.hhu.filmix.dto.roomDTO.request.RegisterRoomRequest;
import com.hhu.filmix.dtoMapper.RoomDTOMapper;
import com.hhu.filmix.entity.Room;
import com.hhu.filmix.enumeration.RoomType;
import com.hhu.filmix.repository.CinemaRepository;
import com.hhu.filmix.repository.RoomRepository;
import com.hhu.filmix.repository.TicketRepository;
import com.hhu.filmix.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoomServicesImpl implements RoomService {
    private final TicketRepository ticketRepository;
    private RoomRepository roomRepository;
    private RoomDTOMapper roomDTOMapper;
    private CinemaRepository cinemaRepository;
    @Override
    public ApiResult<?> registerRoom(RegisterRoomRequest request) {
        if( cinemaRepository.findCinemaById(request.getCinemaId()).isEmpty())
            return ApiResult.fail(ApiCode.NOTFOUND,"影院不存在");

        Room newRoom = roomDTOMapper.toRoom(request);
        newRoom.setDeprecated(false);
        Long roomId = roomRepository.insertRoom(newRoom);
        return ApiResult.data("新建影厅成功", PutResponseDTO.phrase(roomId));
    }

    @Override
    public ApiResult<List<RoomDTO>> getAllRoomByCinema(Long cinemaId) {
        if(cinemaRepository.findCinemaById(cinemaId).isEmpty()){
            return ApiResult.fail(ApiCode.NOTFOUND,"该影院不存在");
        }
        List<RoomDTO> rooms = roomRepository.findRoomsByCinemaId(cinemaId)
                .stream()
                .map(room -> roomDTOMapper.toRoomDTO(room))
                .toList();
        return ApiResult.data(rooms);
    }

    @Transactional
    @Override
    public ApiResult<?> deleteRoom(Long roomId) {
        if(roomRepository.findRoomById(roomId).isEmpty()){
            return ApiResult.fail(ApiCode.NOTFOUND,"该影厅不存在");
        }
        //同时取消影厅相应的电影
        ticketRepository.cancelTicketByRoom(roomId);
        roomRepository.deprecateRoom(roomId);
        return ApiResult.success("影厅删除成功");
    }

    @Override
    public ApiResult<Page<Room>> getDeletedMovies(Integer currentPage, Integer pageSize) {
        Page<Room> roomPage = roomRepository.findDeletedRoom(currentPage,pageSize);
        return ApiResult.data(roomPage);
    }

    @Override
    public ApiResult<?> undeletedMovies(Long id) {
        Room room = roomRepository.findDeletedRoomById(id).orElse(null);
        if (room == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该影厅未被删除");
        }
        roomRepository.undeleteRoom(id);
        return ApiResult.success("影厅已恢复");
    }

    @Override
    public ApiResult<?> permanentDeleteMovie(Long id) {
        Room room = roomRepository.findDeletedRoomById(id).orElse(null);
        if (room == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该影厅未被删除");
        }
        roomRepository.permanentDeleteRoom(id);
        return ApiResult.success("影厅已被永久删除");
    }

    @Override
    public ApiResult<?> editRoom(EditRoomRequest request) {
        Room room = roomRepository.findRoomById(request.getRoomId()).orElse(null);
        if (room == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该影厅不存在");
        }
        if(request.getName()!=null && !request.getName().isEmpty())
            room.setName(request.getName());
        if (request.getRows()!=null)
            room.setRows(request.getRows());
        if(request.getColumns()!=null)
            room.setColumns(request.getColumns());
        if(request.getSupport3D()!=null)
            room.setSupport3D(request.getSupport3D());
        if(request.getRoomType()!=null && !request.getRoomType().equals(RoomType.Normal))
            room.setRoomType(request.getRoomType());
        roomRepository.updateRoom(room);

        return ApiResult.success("影厅修改成功");
    }

    public RoomServicesImpl(RoomRepository roomRepository,
                            RoomDTOMapper roomDTOMapper,
                            CinemaRepository cinemaRepository,
                            TicketRepository ticketRepository) {
        this.ticketRepository =ticketRepository;
        this.roomRepository = roomRepository;
        this.roomDTOMapper = roomDTOMapper;
        this.cinemaRepository = cinemaRepository;
    }
}
