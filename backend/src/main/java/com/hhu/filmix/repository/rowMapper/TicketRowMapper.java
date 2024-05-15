package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.Ticket;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class TicketRowMapper implements RowMapper<Ticket> {
    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Ticket(
                rs.getLong("ticket_id"),
                rs.getLong("movie_id"),
                rs.getObject("show_time", LocalDateTime.class),
                rs.getObject("end_time",LocalDateTime.class),
                rs.getLong("cinema_id"),
                rs.getLong("room_id"),
                rs.getBigDecimal("ticket_price"),
                rs.getBoolean("canceled")
        );
    }
}
