package com.hhu.filmix.service;

import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.cinemaDTO.response.CinemaTicketDTO;
import com.hhu.filmix.dto.movieDTO.response.MovieBriefInfo;
import com.hhu.filmix.dto.ticketDTO.request.NewTicketRequest;
import com.hhu.filmix.dto.ticketDTO.response.TicketDetailDTO;

import java.time.LocalDate;
import java.util.List;

public interface TicketService {
    ApiResult<List<CinemaTicketDTO>> queryMovieTickets(Long movieId, LocalDate date);

    ApiResult<?> queryShowDate(Long movieId);

    ApiResult<TicketDetailDTO> getTicketFullInfo(Long ticketId);

    ApiResult<?> newTicket(NewTicketRequest newTicketRequest);

    ApiResult<?> cancelTicket(Long ticketId);

    ApiResult<List<MovieBriefInfo>> nowShowingMovie();

    ApiResult<List<MovieBriefInfo>> queryUpComingMovies();

    ApiResult<?> getNowShowingMovieByCinema(Long cinemaId);
}
