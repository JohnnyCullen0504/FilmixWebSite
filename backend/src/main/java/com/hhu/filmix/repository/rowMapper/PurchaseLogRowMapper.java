package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.PurchaseLog;
import com.hhu.filmix.enumeration.OrderStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class PurchaseLogRowMapper implements RowMapper<PurchaseLog> {
    @Override
    public PurchaseLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new PurchaseLog(rs.getLong("log_id"),
                rs.getLong("purchase_id"),
                rs.getObject("change_at", LocalDateTime.class),
                OrderStatus.valueOf( rs.getString("order_status_from")),
                OrderStatus.valueOf(rs.getString("order_status_to")),
                rs.getString("note"));
    }
}
