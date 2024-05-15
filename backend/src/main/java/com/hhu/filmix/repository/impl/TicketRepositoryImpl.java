package com.hhu.filmix.repository.impl;

import com.hhu.filmix.dto.ticketDTO.serviceTransfer.TicketDetail;
import com.hhu.filmix.entity.Cinema;
import com.hhu.filmix.entity.Movie;
import com.hhu.filmix.entity.Ticket;
import com.hhu.filmix.repository.TicketRepository;
import com.hhu.filmix.repository.rowMapper.TicketRowMapper;
import com.hhu.filmix.util.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository
public class TicketRepositoryImpl implements TicketRepository {
    private JdbcTemplate jdbcTemplate;
    private TicketRowMapper ticketRowMapper;
    private SnowFlake snowFlake;

    public TicketRepositoryImpl(JdbcTemplate jdbcTemplate,
                                TicketRowMapper ticketRowMapper,
                                SnowFlake snowFlake) {
        this.jdbcTemplate = jdbcTemplate;
        this.ticketRowMapper = ticketRowMapper;
        this.snowFlake = snowFlake;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        var sql = """
                SELECT * FROM ticket WHERE ticket_id = ?;
                """;
        return jdbcTemplate.query(sql, ticketRowMapper,id).stream().findFirst();
    }

    @Override
    public Long insertTicket(Ticket ticket) {
        ticket.setId(snowFlake.genId());
        var sql = """
               INSERT INTO ticket(ticket_id,movie_id,show_time,end_time,cinema_id,canceled,room_id,ticket_price)
               VALUES (?,?,?,?,?,?,?,?);
               """;
        Object[] args ={
                ticket.getId(),
                ticket.getMovieId(),
                ticket.getShowTime(),
                ticket.getEndTime(),
                ticket.getCinemaId(),
                ticket.getCanceled(),
                ticket.getRoomId(),
                ticket.getPrice()
        };
        jdbcTemplate.update(sql,args);
        return ticket.getId();
    }

    @Override
    public void updateMovieTie(Ticket ticket) {
        var sql = """
               UPDATE ticket SET show_time = ?, end_time = ?, cinema_id = ?, room_id = ?, canceled =?,ticket_price = ? WHERE ticket_id = ?;
               """;
        Object[] args ={
                ticket.getShowTime(),
                ticket.getEndTime(),
                ticket.getCinemaId(),
                ticket.getRoomId(),
                ticket.getCanceled(),
                ticket.getPrice(),
                ticket.getId()
        };
        jdbcTemplate.update(sql,args);
    }

    @Override
    public void deleteTicket(Long id) {
        var sql = """
                DELETE FROM ticket WHERE ticket_id = ?;
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public List<TicketDetail> findByMovieAndDateBetween(Long movieId, LocalDateTime begin, LocalDateTime end) {
        var sql = """
SELECT
    T.ticket_id AS T_id, T.show_time AS show_time, T.end_time AS end_time, T.room_id AS room_id,T.ticket_price AS price,T.canceled AS canceled,
    C.cinema_id AS C_id, C.cinema_name AS C_name, C.address AS C_address,
    M.movie_id AS M_id, M.movie_name AS M_name
FROM ticket T
    LEFT JOIN cinema C ON C.cinema_id = T.cinema_id
    LEFT JOIN movie  M ON M.movie_id = T.movie_id
WHERE T.movie_id = ?
AND T.canceled = false
AND T.show_time >= ?
AND T.show_time < ?
                """;
        List<Object> args = new ArrayList<>();
        args.add(movieId);
        args.add(begin);
        args.add(end);
        //构造数据库返回参数映射器
        RowMapper<TicketDetail> detailRowMapper = (rs, rn)->{
            Movie movie = new Movie();
            movie.setId(rs.getLong("M_id"));
            movie.setName("M.name");

            Cinema cinema = new Cinema();
            cinema.setId(rs.getLong("C_id"));
            cinema.setName(rs.getString("C_name"));
            cinema.setAddress(rs.getString("C_address"));

            TicketDetail ticketDetail = new TicketDetail();
            ticketDetail
                    .id(rs.getLong("T_id"))
                    .showTime(rs.getObject("show_time", LocalDateTime.class))
                    .endTime(rs.getObject("end_time", LocalDateTime.class))
                    .cinema(cinema)
                    .movie(movie)
                    .roomId(rs.getLong("room_id"))
                    .price(rs.getBigDecimal("price"))
                    .canceled(rs.getBoolean("canceled"));
            return ticketDetail;
        };
        return jdbcTemplate.query(sql,detailRowMapper,args.toArray());
    }

    @Override
    public List<LocalDate> findShowDateByMovie(Long movieId) {
        var sql = """
                SELECT DATE_FORMAT(show_time,'%Y-%m-%d') date FROM ticket WHERE movie_id = ? AND canceled = false GROUP BY date;
                """;
        RowMapper<LocalDate> dateRowMapper =(rs, rowNum) -> LocalDate.parse(rs.getString("date"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return jdbcTemplate.query(sql,dateRowMapper, movieId);
    }

    @Override
    public Optional<Ticket> checkRoomTimeConflict(Long roomId, LocalDateTime showTime, LocalDateTime endTime) {
        var sql = """
            SELECT * FROM ticket
            WHERE room_id = ?
                AND  canceled = false
                AND ((show_time BETWEEN ? AND ?) OR (end_time BETWEEN ? AND ?) ) ;
                """;
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        Object[] args = {
                roomId,
                showTime.format(fmt),
                endTime.format(fmt),
                showTime.format(fmt),
                endTime.format(fmt),
        };
        return jdbcTemplate.query(sql,ticketRowMapper,args).stream().findFirst();
    }

    @Override
    public void cancelTicketByRoom(Long roomId) {
        var sql = """
                UPDATE ticket SET canceled = true WHERE room_id = ? AND  show_time > NOW();
                """;
        jdbcTemplate.update(sql,roomId);
    }

    @Override
    public void canceledTicketByCinema(Long cinemaId) {
        var sql = """
                UPDATE ticket SET canceled = true WHERE cinema_id = ? AND  show_time > NOW();
                """;
        jdbcTemplate.update(sql,cinemaId);
    }
}
