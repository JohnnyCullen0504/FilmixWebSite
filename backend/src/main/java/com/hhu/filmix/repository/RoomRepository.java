package com.hhu.filmix.repository;

import com.hhu.filmix.api.Page;
import com.hhu.filmix.entity.Room;

import java.util.Optional;
import java.util.List;

public interface RoomRepository {
    Optional<Room> findRoomById(Long id);
    List<Room> findRoomsByCinemaId(Long cinemaId);
    Long insertRoom(Room room);
    void updateRoom(Room room);
    void deleteRoom(Long id);

    void deprecateRoom(Long id);

    void permanentDeleteRoom(Long id);

    Page<Room> findDeletedRoom(Integer currentPage, Integer pageSize);

    Optional<Room> findDeletedRoomById(Long id);

    void undeleteRoom(Long id);

    void deprecateRoomByCinema(Long cinemaId);
}
