package com.hhu.filmix.repository.impl;

import com.hhu.filmix.entity.PurchaseLog;
import com.hhu.filmix.repository.PurchaseLogRepository;
import com.hhu.filmix.repository.rowMapper.PurchaseLogRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class PurchaseLogRepositoryImpl implements PurchaseLogRepository {
    private JdbcTemplate jdbcTemplate;
    private PurchaseLogRowMapper purchaseLogRowMapper;
    @Override
    public List<PurchaseLog> findLogByPurchaseId(Long purchaseId) {
        var sal = """
                SELECT * FROM purchase_log WHERE purchase_id = ?
                """;
        return jdbcTemplate.query(sal,purchaseLogRowMapper,purchaseId);
    }

    public PurchaseLogRepositoryImpl(JdbcTemplate jdbcTemplate, PurchaseLogRowMapper purchaseLogRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.purchaseLogRowMapper = purchaseLogRowMapper;
    }
}
