package com.hhu.filmix.service.impl;

import com.hhu.filmix.api.ApiCode;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.MovieTagDTO.response.MovieTagDTO;
import com.hhu.filmix.dto.PutResponseDTO;
import com.hhu.filmix.dtoMapper.MovieTagDTOMapper;
import com.hhu.filmix.entity.MovieTag;
import com.hhu.filmix.repository.MovieTagRepository;
import com.hhu.filmix.service.TagService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {
    private MovieTagRepository movieTagRepository;
    private MovieTagDTOMapper movieTagDTOMapper;
    @Override
    public ApiResult<?> newMovieTag(String name, String nameZH) {
       Long id =  movieTagRepository.insertTag(new MovieTag(null,name,nameZH));
       return ApiResult.data("新建标签成功", PutResponseDTO.phrase(id));
    }

    @Override
    public ApiResult<?> deleteMovieTag(Long id) {
        MovieTag movieTag = movieTagRepository.findTagById(id).orElse(null);
        if (movieTag!=null){
            return ApiResult.fail(ApiCode.NOTFOUND,"标签不存在");
        }
        movieTagRepository.deleteTag(id);
        //确保数据完整性，删除该标签与电影的关系
        movieTagRepository.clearRelationShipForTag(id);
        return ApiResult.success("删除标签成功");
    }

    @Override
    public ApiResult<List<MovieTagDTO>> getAllTags() {
        List<MovieTag> movieTagList = movieTagRepository.getAllTages();
        List<MovieTagDTO> movieTagDTOS = movieTagList.stream().map(movieTag ->{return movieTagDTOMapper.toMovieTagDTO(movieTag);}).collect(Collectors.toList());
        return ApiResult.data("获取标签成功",movieTagDTOS);
    }

    public TagServiceImpl(MovieTagRepository movieTagRepository,
                          MovieTagDTOMapper movieTagDTOMapper) {
        this.movieTagRepository = movieTagRepository;
        this.movieTagDTOMapper = movieTagDTOMapper;
    }
}
