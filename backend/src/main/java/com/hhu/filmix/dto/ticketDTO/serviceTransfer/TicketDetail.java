package com.hhu.filmix.dto.ticketDTO.serviceTransfer;

import com.hhu.filmix.entity.Cinema;
import com.hhu.filmix.entity.Movie;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@Accessors(fluent = true)
public class TicketDetail {
    private Long id;
    private Movie movie;
    private Cinema cinema;

    private LocalDateTime showTime;
    private LocalDateTime endTime;

    private Long roomId;
    private String roomName;
    private Boolean canceled;
    private BigDecimal price;
}
