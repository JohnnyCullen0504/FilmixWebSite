package com.hhu.filmix.repository.impl;

import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseBriefInfo;
import com.hhu.filmix.dto.purchaseDTO.response.PurchaseDetailInfo;
import com.hhu.filmix.dtoMapper.PurchaseSeatDTOMapper;
import com.hhu.filmix.entity.Purchase;
import com.hhu.filmix.enumeration.OrderStatus;
import com.hhu.filmix.repository.PurchaseLogRepository;
import com.hhu.filmix.repository.PurchaseRepository;
import com.hhu.filmix.repository.PurchaseSeatRepository;
import com.hhu.filmix.repository.rowMapper.PurchaseRowMapper;
import com.hhu.filmix.util.PagingTool;
import com.hhu.filmix.util.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {
    private JdbcTemplate jdbcTemplate;
    private SnowFlake snowFlake;
    private RowMapper<Purchase> purchaseRowMapper;
    private PurchaseSeatRepository purchaseSeatRepository;
    private PurchaseSeatDTOMapper purchaseSeatDTOMapper;
    private PurchaseLogRepository purchaseLogRepository;

    @Override
    public Long insertPurchase(Purchase purchase) {
        purchase.setId(snowFlake.genId());
        var sql = """
                INSERT INTO purchase(purchase_id,user_id,ticket_id,order_status,order_price,trading_time) VALUES(?,?,?,?,?,?);
                """;
        Object[] args={
                purchase.getId(),
                purchase.getUserId(),
                purchase.getTicketId(),
                purchase.getStatus().getName(),
                purchase.getPrice(),
                purchase.getTradingTime()
        };
        jdbcTemplate.update(sql,args);
        return purchase.getId();
    }

    @Override
    public Optional<Purchase> findByUserAndTicket(Long userId, Long ticketId) {
        var sql= """
                SELECT * FROM purchase WHERE user_id = ? AND ticket_id = ?;
                """;
        return jdbcTemplate.query(sql,purchaseRowMapper,userId,ticketId).stream().findFirst();
    }

    @Override
    public void update(Purchase purchase) {
        var sql = """
                UPDATE purchase SET user_id = ?, ticket_id = ?, order_status = ?, order_price = ? WHERE purchase_id = ?;
                """;
        Object[] args={
                purchase.getUserId(),
                purchase.getTicketId(),
                purchase.getStatus().getName(),
                purchase.getPrice(),
                purchase.getId()
        };
        jdbcTemplate.update(sql,args);
    }

    @Override
    public Page<PurchaseBriefInfo> findUserPurchases(Long userId, Integer pageSize, Integer currentPage) {
        var countSql = """
                SELECT COUNT(purchase_id) FROM purchase WHERE user_id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(countSql,Integer.TYPE,userId);

        Integer offset = PagingTool.offset(pageSize,currentPage);
        RowMapper<PurchaseBriefInfo> purchaseBriefInfoRowMapper = (rs,rn)->{
            return new PurchaseBriefInfo(
                    rs.getLong("id"),
                    rs.getLong("ticket_id"),
                    rs.getString("movie_name"),
                    rs.getString("cinema_name"),
                    OrderStatus.valueOf(rs.getString("status")),
                    rs.getObject("show_time", LocalDateTime.class),
                    rs.getBigDecimal("price")
            );
        };
        var sql= """
                SELECT 
                p.purchase_id AS id,p.ticket_id AS ticket_id,p.order_status AS status,p.order_price AS price,
                t.show_time AS show_time,
                m.movie_name AS movie_name,
                c.cinema_name AS cinema_name
                FROM purchase p
                LEFT JOIN ticket t on t.ticket_id = p.ticket_id
                LEFT JOIN cinema c on c.cinema_id = t.cinema_id
                LEFT JOIN movie m on m.movie_id = t.movie_id
                WHERE p.user_id = ?
                LIMIT ?,?
                """;
        List<PurchaseBriefInfo> records = jdbcTemplate.query(sql,purchaseBriefInfoRowMapper,userId,offset,pageSize);
        Page<PurchaseBriefInfo> page = Page.<PurchaseBriefInfo>builder()
                .totalPage(PagingTool.totalPage(count,pageSize))
                .currentPage(currentPage)
                .pageSize(pageSize)
                .records(records)
                .build();
        return page;
    }

    @Override
    public Optional<Purchase> findPurchaseById(Long purchaseId) {
        var sql = """
                SELECT * FROM purchase WHERE purchase_id = ?;
                """;
        return jdbcTemplate.query(sql,purchaseRowMapper,purchaseId).stream().findFirst();
    }

    @Override
    public Optional<PurchaseDetailInfo> findPurchaseDetail(Long purchaseId) {
        var sql = """
                SELECT
                p.purchase_id AS id,p.ticket_id AS ticket_id,p.order_status AS status,p.order_price AS price,p.trading_time AS trading_time,
                t.show_time AS show_time,
                m.movie_name AS movie_name,
                c.cinema_name AS cinema_name,
                r.room_name AS room_name
                FROM purchase p
                LEFT JOIN ticket t on t.ticket_id = p.ticket_id
                LEFT JOIN cinema c on c.cinema_id = t.cinema_id
                LEFT JOIN movie m on m.movie_id = t.movie_id
                LEFT JOIN filmix.room r on r.room_id = t.room_id
                WHERE p.purchase_id = ?
                """;
        RowMapper<PurchaseDetailInfo> purchaseDetailInfoRowMapper = (rs,rn)->{
            return new PurchaseDetailInfo(
                    rs.getLong("id"),
                    rs.getLong("ticket_id"),
                    purchaseSeatRepository.findByPurchaseId(purchaseId)
                            .stream()
                            .map(purchaseSeat -> purchaseSeatDTOMapper.toPurchaseSeatInfo(purchaseSeat))
                            .collect(Collectors.toList()),
                    rs.getString("movie_name"),
                    rs.getString("cinema_name"),
                    rs.getString("room_name"),
                    OrderStatus.valueOf(rs.getString("status")),
                    rs.getObject("show_time", LocalDateTime.class),
                    rs.getBigDecimal("price"),
                    purchaseLogRepository.findLogByPurchaseId(purchaseId),
                    rs.getObject("trading_time",LocalDateTime.class)

            );
        };
        return jdbcTemplate.query(sql,purchaseDetailInfoRowMapper,purchaseId).stream().findFirst();
    }

    public PurchaseRepositoryImpl(JdbcTemplate jdbcTemplate,
                                  SnowFlake snowFlake,
                                  PurchaseRowMapper purchaseRowMapper,
                                  PurchaseSeatRepository purchaseSeatRepository,
                                  PurchaseSeatDTOMapper purchaseSeatDTOMapper,
                                  PurchaseLogRepository purchaseLogRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.snowFlake = snowFlake;
        this.purchaseRowMapper = purchaseRowMapper;
        this.purchaseSeatRepository = purchaseSeatRepository;
        this.purchaseSeatDTOMapper = purchaseSeatDTOMapper;
        this.purchaseLogRepository = purchaseLogRepository;
    }
}
