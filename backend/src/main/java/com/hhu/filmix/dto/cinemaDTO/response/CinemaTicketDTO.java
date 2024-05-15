package com.hhu.filmix.dto.cinemaDTO.response;

import com.hhu.filmix.dto.ticketDTO.response.TicketBriefDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用于承载一场电影的一个影院单日的所有排期")
public class CinemaTicketDTO {
    private CinemaDTO cinema;
    private List<TicketBriefDTO> tickets;
}
