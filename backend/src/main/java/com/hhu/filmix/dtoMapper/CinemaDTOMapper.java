package com.hhu.filmix.dtoMapper;

import com.hhu.filmix.dto.cinemaDTO.response.CinemaDTO;
import com.hhu.filmix.entity.Cinema;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
@Mapper(componentModel = "spring")
public interface CinemaDTOMapper {
    CinemaDTOMapper cinemaDTOMapperImpl = Mappers.getMapper(CinemaDTOMapper.class);
    @Mapping(source = "id",target = "cinemaId")
    CinemaDTO toCinemaDTO(Cinema cinema);
}
