package com.hhu.filmix.dtoMapper;

import com.hhu.filmix.dto.roomDTO.RoomDTO;
import com.hhu.filmix.dto.roomDTO.request.RegisterRoomRequest;
import com.hhu.filmix.entity.Room;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomDTOMapper {
    @Mapping(target ="roomType" ,expression = "java(room.getRoomType().getName())")
    @Mapping(source = "id",target = "roomId")
    RoomDTO toRoomDTO(Room room);

    Room toRoom(RegisterRoomRequest registerRoomRequest);
}
