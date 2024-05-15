package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.Purchase;
import com.hhu.filmix.enumeration.OrderStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class PurchaseRowMapper implements RowMapper<Purchase> {

    @Override
    public Purchase mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Purchase(rs.getLong("purchase_id"),
                rs.getLong("user_id"),
                rs.getLong("ticket_id"),
                OrderStatus.valueOf(rs.getString("order_status")),
                rs.getBigDecimal("order_price"),
                rs.getObject("trading_time", LocalDateTime.class));
    }
}
