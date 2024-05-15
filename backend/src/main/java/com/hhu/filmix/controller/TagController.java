package com.hhu.filmix.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.hhu.filmix.annotation.authentication.CheckLogin;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.MovieTagDTO.response.MovieTagDTO;
import com.hhu.filmix.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ApiResponse(useReturnTypeSchema = true)
@Tag(name = "电影标签Api")
public class TagController {
    private TagService tagService;
    @PutMapping("/tag")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"ADMIN","STAFF"})
    @Operation(summary = "新增电影标签")
    public ApiResult<?> newMovieTage(@RequestParam("name")@Parameter(description = "标签中文名") String name,
                                     @RequestParam("nameZH")@Parameter(description = "标签英文名")String nameZH){
        return tagService.newMovieTag(name,nameZH);

    }
    @DeleteMapping("/tag")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"ADMIN","STAFF"})
    @Operation(summary = "删除电影标签")
    public ApiResult<?> deleteMovieTag(@RequestParam("id") Long id){
        return tagService.deleteMovieTag(id);
    }
    @GetMapping("/tag")
    @Operation(summary = "获取所有电影标签")
    public ApiResult<List<MovieTagDTO>> getAllMovieTag(){
        return tagService.getAllTags();
    }

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
}
