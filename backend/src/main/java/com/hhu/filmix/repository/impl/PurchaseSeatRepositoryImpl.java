package com.hhu.filmix.repository.impl;

import com.hhu.filmix.entity.PurchaseSeat;
import com.hhu.filmix.repository.PurchaseSeatRepository;
import com.hhu.filmix.repository.rowMapper.PurchaseSeatRowMapper;
import com.hhu.filmix.util.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PurchaseSeatRepositoryImpl implements PurchaseSeatRepository {
    private SnowFlake snowFlake;
    private PurchaseSeatRowMapper purchaseSeatRowMapper;
    private JdbcTemplate jdbcTemplate;

    @Override
    public void insertPurchaseSeat(List<PurchaseSeat> purchaseSeats) {
        var sql = """
                INSERT INTO purchase_seat(purchase_seat_id,ticket_id,purchase_id,ro,col,valid) VALUES (?,?,?,?,?,?);
                """;
        List<Object[]> batchArgs = purchaseSeats
                .stream()
                .map(purchaseSeat -> {
                    purchaseSeat.setId(snowFlake.genId());
                    purchaseSeat.setValid(true);
                    Object[] args ={
                            purchaseSeat.getId(),
                            purchaseSeat.getTicketId(),
                            purchaseSeat.getPurchaseId(),
                            purchaseSeat.getRow(),
                            purchaseSeat.getColumn(),
                            purchaseSeat.getValid()
                    };
                    return args;
                }).toList();
        jdbcTemplate.batchUpdate(sql,batchArgs);
    }

    @Override
    public List<PurchaseSeat> findValidSeatByTicketId(Long ticketId) {
        var sql = """
                SELECT * FROM purchase_seat WHERE ticket_id = ?;
                """;
        return jdbcTemplate.query(sql,purchaseSeatRowMapper,ticketId);
    }

    @Override
    public Optional<PurchaseSeat> findByTicketAndSeatAndIsValid(Long ticketId, Integer row, Integer column) {
        var sql = """
                SELECT * FROM purchase_seat 
                WHERE ticket_id = ? AND ro = ? AND col = ? AND valid = true;
                """;
        return jdbcTemplate.query(sql,purchaseSeatRowMapper,ticketId,row,column).stream().findFirst();
    }

    @Override
    public List<PurchaseSeat> findByPurchaseId(Long purchaseId) {
        var sql = """
                SELECT * FROM purchase_seat WHERE purchase_id = ?;
                """;
        return jdbcTemplate.query(sql,purchaseSeatRowMapper,purchaseId);
    }

    @Override
    public void invalidate(Long purchaseId) {
        var sql = """
                UPDATE purchase_seat SET valid = false WHERE purchase_id = ?
                """;
        jdbcTemplate.update(sql,purchaseId);
    }

    public PurchaseSeatRepositoryImpl(SnowFlake snowFlake,
                                      PurchaseSeatRowMapper purchaseSeatRowMapper,
                                      JdbcTemplate jdbcTemplate) {
        this.snowFlake = snowFlake;
        this.purchaseSeatRowMapper = purchaseSeatRowMapper;
        this.jdbcTemplate = jdbcTemplate;
    }
}
