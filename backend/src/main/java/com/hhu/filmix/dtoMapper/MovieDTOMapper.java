package com.hhu.filmix.dtoMapper;

import com.hhu.filmix.dto.MovieTagDTO.response.MovieTagDTO;
import com.hhu.filmix.dto.movieDTO.response.MovieBriefInfo;
import com.hhu.filmix.dto.movieDTO.response.MovieDTO;
import com.hhu.filmix.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring")
public interface MovieDTOMapper  {
    default MovieDTO toMovieDTO(Movie movie, List<MovieTagDTO> movieTagDTOList){
        return new MovieDTO(
                movie.getId(),
                movie.getName(),
                movie.getDuration(),
                movie.getReleaseTime(),
                movieTagDTOList,
                movie.getPosterURL(),
                movie.getLanguage(),
                movie.getLanguage().getName(),
                movie.getLanguage().getZH_name(),
                movie.getSource()
        );
    }
    @Mapping(source = "id",target = "movieId")
    MovieBriefInfo toMovieBriefInfo(Movie movie);
}
