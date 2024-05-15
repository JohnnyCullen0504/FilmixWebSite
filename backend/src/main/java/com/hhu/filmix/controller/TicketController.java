package com.hhu.filmix.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hhu.filmix.annotation.authentication.CheckLogin;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.cinemaDTO.response.CinemaTicketDTO;
import com.hhu.filmix.dto.ticketDTO.request.NewTicketRequest;
import com.hhu.filmix.dto.ticketDTO.response.TicketDetailDTO;
import com.hhu.filmix.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@ApiResponse(useReturnTypeSchema = true)
@Tag(name = "电影排期Api")
public class TicketController {
    private TicketService ticketService;
    @GetMapping("/movie/{id}/tickets")
    @Operation(summary = "查询单日电影排期")
    public ApiResult<List<CinemaTicketDTO>> getTicket(@PathVariable("id")@Parameter(description = "电影id") Long movieId,
                                                      @NotNull(message = "日期不能为空") @RequestParam(value = "date")@JsonFormat(pattern = "yyyy-MM-dd") @Schema(pattern = "yyyy-MM-dd") LocalDate date){
        return ticketService.queryMovieTickets(movieId, date);
    }
    @GetMapping("/movie/{id}/time")
    @Operation(summary = "查询电影所有排期日期")
    public ApiResult<?> getTicket(@PathVariable("id")@Parameter(description = "电影id") Long movieId){
        return ticketService.queryShowDate(movieId);
    }
    @GetMapping("movie/ticket/{ticket_id}")
    @Operation(summary = "查询单个电影排期的详细数据")
    public ApiResult<TicketDetailDTO> getTicketFullInfo(@Parameter(description = "电影排期id")@PathVariable("ticket_id") Long ticketId){
        return ticketService.getTicketFullInfo(ticketId);
    }
    @PutMapping("ticket")
    @Operation(summary = "新增电影排期")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"ADMIN","STAFF"})
    public ApiResult<?> newTicket(@Valid @RequestBody NewTicketRequest newTicketRequest){
       return ticketService.newTicket(newTicketRequest);
    }
    @PostMapping("ticket/cancel")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"ADMIN","STAFF"})
    @Operation(summary = "取消电影排期")
    public ApiResult<?> cancelTicket(@RequestParam("id")Long ticketId){
        return ticketService.cancelTicket(ticketId);
    }

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
}
