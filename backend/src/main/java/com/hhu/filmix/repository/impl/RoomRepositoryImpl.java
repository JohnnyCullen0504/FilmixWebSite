package com.hhu.filmix.repository.impl;

import com.hhu.filmix.api.Page;
import com.hhu.filmix.entity.Room;
import com.hhu.filmix.repository.RoomRepository;
import com.hhu.filmix.util.PagingTool;
import com.hhu.filmix.util.SnowFlake;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RoomRepositoryImpl implements RoomRepository {
    private JdbcTemplate jdbcTemplate;
    private SnowFlake snowFlake;
    private RowMapper<Room> roomRowMapper;

    @Override
    public Optional<Room> findRoomById(Long id) {
       var sql = """
                SELECT * FROM room WHERE room_id = ? AND room_deprecated = false
                """;
       return  jdbcTemplate.query(sql,roomRowMapper,id).stream().findFirst();
    }

    @Override
    public List<Room> findRoomsByCinemaId(Long cinemaId) {
        var sql = """
                SELECT * FROM room WHERE cinema_id = ? AND room_deprecated = false
                """;
        return jdbcTemplate.query(sql,roomRowMapper,cinemaId);
    }

    @Override
    public Long insertRoom(Room room) {
        room.setId(snowFlake.genId());
        var sql= """
                INSERT INTO room(room_id,cinema_id,room_name,ros,cols,room_type,support_3D,room_deprecated) VALUES (?,?,?,?,?,?,?,?);
                """;
        Object[] args = {
                room.getId(),
                room.getCinemaId(),
                room.getName(),
                room.getRows(),
                room.getColumns(),
                room.getRoomType().getName(),
                room.getSupport3D(),
                room.getDeprecated()
        };
        jdbcTemplate.update(sql,args);

        return room.getId();
    }

    @Override
    public void updateRoom(Room room) {
        var sql = """
                UPDATE room SET cinema_id = ?,room_name=?,ros=?,cols=?,room_type=?,support_3D=? WHERE room_id = ?;
                """;
        Object[] args = {
                room.getCinemaId(),
                room.getName(),
                room.getRows(),
                room.getColumns(),
                room.getRoomType().getName(),
                room.getSupport3D(),
                room.getId()
        };
        jdbcTemplate.update(sql,args);
    }

    @Override
    public void deleteRoom(Long id) {
        var sql = """
                DELETE FROM room WHERE room_id = ?;
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public void deprecateRoom(Long id) {
        var sql = """
                UPDATE room SET room_deprecated = true WHERE room_id = ?
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public void permanentDeleteRoom(Long id) {
        var sql = """
                DELETE FROM room WHERE room_id = ?;
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public Page<Room> findDeletedRoom(Integer currentPage, Integer pageSize) {
        Integer offset = PagingTool.offset(pageSize,currentPage);
        var querySql = "SELECT * FROM room WHERE room_deprecated = TRUE";
        var countSql = "SELECT COUNT(*) AS c FROM ("+querySql +") AS t";
        var pagingSql = "SELECT * FROM ("+ querySql+") AS t LIMIT ?,?";
        Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
        List<Room> rooms = jdbcTemplate.query(pagingSql,roomRowMapper,offset,pageSize);
        return Page.<Room>builder()
                .records(rooms)
                .currentPage(currentPage)
                .totalPage(PagingTool.totalPage(count,pageSize))
                .build();
    }

    @Override
    public Optional<Room> findDeletedRoomById(Long id) {
        var sql = """
                SELECT * FROM room WHERE room_deprecated = TRUE AND room_id = ?
                """;
        return jdbcTemplate.query(sql,roomRowMapper,id).stream().findFirst();
    }

    @Override
    public void undeleteRoom(Long id) {
        var sql = """
                UPDATE room SET room_deprecated = FALSE WHERE  room_id = ?
                """;
        jdbcTemplate.update(sql,id);
    }

    @Override
    public void deprecateRoomByCinema(Long cinemaId) {
        var sql = """
                UPDATE  room SET room_deprecated = true WHERE cinema_id = ?
                """;
        jdbcTemplate.update(sql,cinemaId);
    }

    public RoomRepositoryImpl(JdbcTemplate jdbcTemplate, SnowFlake snowFlake, RowMapper<Room> roomRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.snowFlake = snowFlake;
        this.roomRowMapper = roomRowMapper;
    }
}
