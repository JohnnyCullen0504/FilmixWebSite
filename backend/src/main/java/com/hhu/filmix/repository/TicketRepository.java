package com.hhu.filmix.repository;

import com.hhu.filmix.dto.ticketDTO.serviceTransfer.TicketDetail;
import com.hhu.filmix.entity.Ticket;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TicketRepository {
    Optional<Ticket> findById(Long id);
    Long insertTicket(Ticket ticket);
    void updateMovieTie(Ticket ticket);
    void deleteTicket(Long id);
    List<TicketDetail> findByMovieAndDateBetween(Long movieId, LocalDateTime begin, LocalDateTime end);

    List<LocalDate> findShowDateByMovie(Long movieId);

    Optional<Ticket> checkRoomTimeConflict(Long roomId, LocalDateTime showTime, LocalDateTime endTime);

    void cancelTicketByRoom(Long roomId);

    void canceledTicketByCinema(Long cinemaId);
}
