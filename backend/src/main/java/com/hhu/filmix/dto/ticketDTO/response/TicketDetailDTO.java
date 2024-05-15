package com.hhu.filmix.dto.ticketDTO.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.hhu.filmix.dto.cinemaDTO.response.CinemaDTO;
import com.hhu.filmix.dto.movieDTO.response.MovieDTO;
import com.hhu.filmix.dto.roomDTO.RoomDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "电影排期的详细信息")
public class TicketDetailDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "电影排期编号")
    private Long ticketId;

    @Schema(description = "电影开始放映时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime showTime;

    @Schema(description = "散场时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @Schema(description = "电影信息")
    private MovieDTO movie;

    @Schema(description = "影院信息")
    private CinemaDTO cinema;

    @Schema(description = "影厅信息")
    private RoomDTO room;
    @Schema(description = "价格")
    private BigDecimal price;
    @Schema(description = "已出售座位(row,column)")
    private List<String> sold;
    @Schema(description = "排期是否取消")
    private Boolean canceled;
}
