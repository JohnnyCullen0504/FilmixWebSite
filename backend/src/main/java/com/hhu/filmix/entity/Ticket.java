package com.hhu.filmix.entity;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class Ticket {
    private Long id;
    private Long movieId;
    private LocalDateTime showTime;
    private LocalDateTime endTime;
    private Long cinemaId;
    private Long roomId;
    private BigDecimal price;
    private Boolean canceled;

    public Ticket(Long id, Long movieId, LocalDateTime showTime, LocalDateTime endTime, Long cinemaId, Long roomId) {
        this.id = id;
        this.movieId = movieId;
        this.showTime = showTime;
        this.endTime = endTime;
        this.cinemaId = cinemaId;
        this.roomId = roomId;
        this.canceled =false;
    }
}
