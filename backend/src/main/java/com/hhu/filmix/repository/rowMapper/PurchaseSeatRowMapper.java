package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.PurchaseSeat;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PurchaseSeatRowMapper implements RowMapper<PurchaseSeat> {
    @Override
    public PurchaseSeat mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PurchaseSeat(
                rs.getLong("purchase_seat_id"),
                rs.getLong("purchase_id"),
                rs.getLong("ticket_id"),
                rs.getInt("ro"),
                rs.getInt("col"),
                rs.getBoolean("valid")
                );
    }
}
