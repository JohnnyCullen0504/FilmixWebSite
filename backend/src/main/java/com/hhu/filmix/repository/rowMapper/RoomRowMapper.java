package com.hhu.filmix.repository.rowMapper;

import com.hhu.filmix.entity.Room;
import com.hhu.filmix.enumeration.RoomType;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class RoomRowMapper implements RowMapper<Room> {
    @Override
    public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Room(
                rs.getLong("room_id"),
                rs.getLong("cinema_id"),
                rs.getString("room_name"),
                rs.getInt("ros"),
                rs.getInt("cols"),
                RoomType.RoomType(rs.getString("room_type")),
                rs.getBoolean("support_3D"),
                rs.getBoolean("room_deprecated"));
    }
}
