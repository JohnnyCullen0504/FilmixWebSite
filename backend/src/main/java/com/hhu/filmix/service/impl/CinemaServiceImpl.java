package com.hhu.filmix.service.impl;

import com.hhu.filmix.api.ApiCode;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.PutResponseDTO;
import com.hhu.filmix.dto.cinemaDTO.request.CinemaEditRequest;
import com.hhu.filmix.dto.cinemaDTO.request.CinemaRegisterRequest;
import com.hhu.filmix.dto.cinemaDTO.response.CinemaDTO;
import com.hhu.filmix.dtoMapper.CinemaDTOMapper;
import com.hhu.filmix.entity.Cinema;
import com.hhu.filmix.repository.CinemaRepository;
import com.hhu.filmix.repository.RoomRepository;
import com.hhu.filmix.repository.TicketRepository;
import com.hhu.filmix.service.CinemaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CinemaServiceImpl implements CinemaService {
    private CinemaRepository cinemaRepository;
    private CinemaDTOMapper cinemaDTOMapper;
    private RoomRepository roomRepository;
    private TicketRepository ticketRepository;
    @Override
    public ApiResult<CinemaDTO> getCinemaInfo(Long CinemaId) {
        Cinema cinema = cinemaRepository.findCinemaById(CinemaId).orElse(null);
        if(cinema == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"影院不存在");
        }
        return ApiResult.data("影院信息查询成功", cinemaDTOMapper.toCinemaDTO(cinema));
    }

    @Override
    public ApiResult<?> registerCinema(CinemaRegisterRequest request) {
        //防止重名重地址影院
        if(!cinemaRepository.findByNameEqual(request.getName()).isEmpty())
            return ApiResult.fail(ApiCode.RESOURCE_CONFLICT,"已有重名影院");
        if(!cinemaRepository.findByAddressEqual(request.getAddress()).isEmpty()) {
            return ApiResult.fail(ApiCode.RESOURCE_CONFLICT,"该地址名已有影院");
        }
         Cinema cinema = new Cinema();
         cinema.setName(request.getName());
         cinema.setAddress(request.getAddress());
         cinema.setDeprecated(false);
         Long cinemaId = cinemaRepository.insertCinema(cinema);
         return ApiResult.data("新增影院成功", PutResponseDTO.phrase(cinemaId));
    }

    @Override
    public ApiResult<?> deleteCinema(Long cinemaId) {
        Cinema cinema = cinemaRepository.findCinemaById(cinemaId).orElse(null);
        if(cinema == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"影院不存在");
        }
        cinemaRepository.deprecatedCinema(cinemaId);
        //同时废弃影院下所有影厅
        //取消影院内的所有未开始电影
        roomRepository.deprecateRoomByCinema(cinemaId);
        ticketRepository.canceledTicketByCinema(cinemaId);
        return ApiResult.success("影院删除成功");

    }

    @Override
    public ApiResult<?> editCinema(CinemaEditRequest request) {
        Cinema cinema = cinemaRepository.findCinemaById(request.getCinemaId()).orElse(null);
        if (cinema == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"影院不存在");
        }
        if(request.getName()!= null && !request.getName().equals("")){
            Cinema sameNameCinema = cinemaRepository.findByNameEqual(request.getName()).stream().findFirst().orElse(null);
            if(sameNameCinema != null && !sameNameCinema.getId().equals(request.getCinemaId()))
                return ApiResult.fail(ApiCode.RESOURCE_CONFLICT,"所修改名字与另一影院重名");
            cinema.setName(request.getName());
        }

        if (request.getAddress()!=null && !request.getAddress().equals("")){
            Cinema sameAddrCinema = cinemaRepository.findByAddressEqual(request.getAddress()).stream().findFirst().orElse(null);
            if(sameAddrCinema != null && !sameAddrCinema.getId().equals(request.getCinemaId()))
                return ApiResult.fail(ApiCode.RESOURCE_CONFLICT,"所修改地址与另一影院重复");
            cinema.setAddress(request.getAddress());
        }

        cinemaRepository.updateCinema(cinema);

        return ApiResult.success("影院信息修改成功");
    }

    @Override
    public ApiResult<Page<CinemaDTO>> getRooms(Integer currentPage, Integer pageSize, String name) {
        Page<Cinema> cinemaPage =null;
        if(name == null){
            cinemaPage = cinemaRepository.findAllCinemas(currentPage,pageSize);
        }else{
            cinemaPage = cinemaRepository.findCinemasByName(currentPage,pageSize,name);
        }
        Page<CinemaDTO> cinemaDTOPage =
                Page.<CinemaDTO>builder()
                        .currentPage(currentPage)
                        .pageSize(pageSize)
                        .totalPage(cinemaPage.getTotalPage())
                        .records(cinemaPage.getRecords().stream().map(cinema -> cinemaDTOMapper.toCinemaDTO(cinema)).toList())
                        .build();
        return ApiResult.data(cinemaDTOPage);
    }

    @Override
    public ApiResult<Page<Cinema>> getDeletedCinemas(Integer currentPage, Integer pageSize) {
        Page<Cinema> cinemaPage = cinemaRepository.findDeletedCinema(currentPage,pageSize);
        return ApiResult.data(cinemaPage);
    }

    @Override
    public ApiResult<?> undeletedCinemas(Long id) {
        Cinema cinema = cinemaRepository.findDeletedCinemaById(id).orElse(null);
        if (cinema == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该影院未被删除");
        }
        cinemaRepository.undeleteCinema(id);
        return ApiResult.success("影院已恢复");
    }

    @Override
    @Transactional
    public ApiResult<?> permanentDeleteCinema(Long id) {
        Cinema cinema = (Cinema) cinemaRepository.findDeletedCinemaById(id).orElse(null);
        if (cinema == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该影院未被（软）删除");
        }
        //删除影院及其下所有影厅
        cinemaRepository.permanentDeleteCinema(id);
        roomRepository.findRoomsByCinemaId(id).stream().forEach(room -> {
            roomRepository.permanentDeleteRoom(room.getId());
        });
        return ApiResult.success("影院及其所有影厅已永久删除");
    }


    public CinemaServiceImpl(CinemaRepository CinemaRepository,
                             CinemaDTOMapper cinemaDTOMapper,
                             RoomRepository roomRepository,
                             TicketRepository ticketRepository) {
        this.cinemaRepository = CinemaRepository;
        this.cinemaDTOMapper = cinemaDTOMapper;
        this.roomRepository = roomRepository;
        this.ticketRepository = ticketRepository;
    }
}
