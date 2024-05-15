package com.hhu.filmix.dtoMapper;

import com.hhu.filmix.dto.MovieTagDTO.response.MovieTagDTO;
import com.hhu.filmix.entity.MovieTag;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovieTagDTOMapper {
    @Mapping(source = "id",target = "tagId")
    MovieTagDTO toMovieTagDTO(MovieTag movieTag);
}
