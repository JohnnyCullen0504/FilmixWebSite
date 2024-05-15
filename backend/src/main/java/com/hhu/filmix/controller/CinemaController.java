package com.hhu.filmix.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.hhu.filmix.annotation.authentication.CheckLogin;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.cinemaDTO.request.CinemaEditRequest;
import com.hhu.filmix.dto.cinemaDTO.request.CinemaRegisterRequest;
import com.hhu.filmix.dto.cinemaDTO.response.CinemaDTO;
import com.hhu.filmix.dto.roomDTO.RoomDTO;
import com.hhu.filmix.dto.roomDTO.request.EditRoomRequest;
import com.hhu.filmix.dto.roomDTO.request.RegisterRoomRequest;
import com.hhu.filmix.entity.Cinema;
import com.hhu.filmix.entity.Room;
import com.hhu.filmix.service.CinemaService;
import com.hhu.filmix.service.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ApiResponse(useReturnTypeSchema = true)
@Tag(name = "影院Api")
public class CinemaController {
    private CinemaService cinemaService;
    private RoomService roomService;

    @CheckLogin
    @SaCheckRole({"ADMIN"})
    @PutMapping("/cinema")
    @Operation(summary = "新增影院")
    public ApiResult<?> registerStore(@Valid @RequestBody CinemaRegisterRequest request){
        return cinemaService.registerCinema(request);
    }
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"ADMIN"})
    @PostMapping("/cinema")
    @Operation(summary = "修改影院信息")
    public ApiResult<?> editStore(@Valid @RequestBody CinemaEditRequest request){
        return cinemaService.editCinema(request);
    }

    @GetMapping("/cinema/{id}")
    @Operation(summary = "获取影院基本信息")
    public ApiResult<CinemaDTO> storeInfo(@PathVariable("id")Long storeId){
        return cinemaService.getCinemaInfo(storeId);
    }
    @GetMapping("/cinema/{id}/rooms")
    @Operation(summary = "获取电影院影厅")
    public ApiResult<List<RoomDTO>> getCinemaRoom(@PathVariable("id")@Parameter(description = "影院id") Long cinemaId){
        return roomService.getAllRoomByCinema(cinemaId);
    }
    @GetMapping("/cinemas")
    @Operation(summary = "获取所有影院")
    public ApiResult<Page<CinemaDTO>> getCinemaRoom(@RequestParam("currentPage")Integer currentPage,
                                                    @RequestParam("pageSize")Integer pageSize,
                                                    @Nullable @RequestParam("name")@Parameter(required = false) String name){
        return cinemaService.getRooms(currentPage,pageSize,name);
    }

    @CheckLogin
    @SaCheckRole({"ADMIN"})
    @DeleteMapping("/cinema")
    @Operation(summary = "删除影院(软删除)")
    public ApiResult<?> deleteStore(@Parameter(description = "门店编号")@RequestParam("id") Long id){
        return cinemaService.deleteCinema(id);
    }

    @GetMapping("cinema/deleted")
    @Operation(summary = "获取已删除的影院")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<Page<Cinema>> getDeletedCinema(@RequestParam("currentPage")Integer currentPage,
                                                    @RequestParam("pageSize")Integer pageSize){
        return cinemaService.getDeletedCinemas(currentPage,pageSize);
    }

    @PostMapping("cinema/undelete")
    @Operation(summary = "恢复已删除的影院")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> UndeletedCinema(@RequestParam("id") Long id){
        return cinemaService.undeletedCinemas(id);
    }

    @DeleteMapping("cinema/permanentDelete")
    @Operation(summary = "永久删除影院")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> permanentDeleteCinema(@RequestParam("id") Long id){
        return cinemaService.permanentDeleteCinema(id);
    }

    @CheckLogin
    @PutMapping("/room")
    @Operation(summary = "新增影厅")
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> registerRoom(@Valid @RequestBody RegisterRoomRequest request){
       return roomService.registerRoom(request);
    }
    @CheckLogin
    @PostMapping("/room")
    @Operation(summary = "修改影厅")
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> editRoom(@Valid @RequestBody EditRoomRequest request){
        return roomService.editRoom(request);
    }

    @CheckLogin
    @SaCheckRole({"ADMIN"})
    @DeleteMapping("/room")
    @Operation(summary = "删除影厅(软删除)")
    public ApiResult<?> deleteRoom(@RequestParam("id") Long id){
        return roomService.deleteRoom(id);
    }

    @GetMapping("room/deleted")
    @Operation(summary = "获取已删除的影厅")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<Page<Room>> getDeletedRoom(@RequestParam("currentPage")Integer currentPage,
                                                @RequestParam("pageSize")Integer pageSize){
        return roomService.getDeletedMovies(currentPage,pageSize);
    }

    @PostMapping("room/undelete")
    @Operation(summary = "恢复已删除的影厅")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> UndeletedRoom(@RequestParam("id") Long id){
        return roomService.undeletedMovies(id);
    }

    @DeleteMapping("room/permanentDelete")
    @Operation(summary = "永久删除影厅")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> permanentDeleteRoom(@RequestParam("id") Long id){
        return roomService.permanentDeleteMovie(id);
    }

    public CinemaController(CinemaService storeService,
                            RoomService roomService) {
        this.cinemaService = storeService;
        this.roomService = roomService;
    }
}
