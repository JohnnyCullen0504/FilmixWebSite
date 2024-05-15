package com.hhu.filmix.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.annotation.SaMode;
import com.hhu.filmix.annotation.authentication.CheckLogin;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.movieDTO.response.MovieBriefInfo;
import com.hhu.filmix.dto.movieDTO.response.MovieDTO;
import com.hhu.filmix.entity.Movie;
import com.hhu.filmix.enumeration.Language;
import com.hhu.filmix.service.MovieService;
import com.hhu.filmix.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@ApiResponse(useReturnTypeSchema = true)
@Tag(name = "电影Api")
public class MovieController {
    private MovieService movieService;
    private TicketService ticketService;
    @GetMapping("movie/{id}")
    @Operation(summary = "获取电影基本信息")
    public ApiResult<MovieDTO> getMovieInfo(@PathVariable("id")@Parameter(description = "电影id") Long movieId){
        return movieService.getMovieInfo(movieId);
    }
    @GetMapping("/movie/nowShowing")
    @Operation(summary = "正在上映的电影")
    public ApiResult<List<MovieBriefInfo>> getNowShowingMovie(){
        return ticketService.nowShowingMovie();
    }
    @GetMapping("/movie/upcoming")
    @Operation(summary ="即将上映的电影" )
    public ApiResult<List<MovieBriefInfo>> getUpcomingMovie(){
        return ticketService.queryUpComingMovies();
    }
    @PutMapping("/movie")
    @CheckLogin
    @Operation(summary = "新增电影")
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> createMovie(@RequestParam("posterFile")
                                    @Parameter(description = "电影海报封面")
                                        MultipartFile posterFile,

                                    @RequestParam("name")
                                    @Parameter(description = "电影名")
                                        String name,

                                    @RequestParam("duration")
                                    @Parameter(description = "电影时长（分钟）")
                                        Integer duration,

                                    @RequestParam("release")
                                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                                    @Parameter(description = "上映时间")
                                        LocalDate release,

                                    @RequestParam("tags")
                                    @Parameter(description = "电影类别")
                                        List<Long> tags,

                                    @RequestParam("language" )
                                    @Parameter(description = "语种")
                                        Language language,

                                    @RequestParam("source")
                                    @Parameter(description = "片源（国家）")
                                        String source
                                        ) {
        return movieService.createNewMovie(name,duration,release,tags,language,source,posterFile);
    }

    @PostMapping("/movie")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    @Operation(summary = "修改电影信息")
    public ApiResult<?> editMovie(@RequestParam("id")
                                      @Parameter(description = "电影id")
                                      @Nonnull
                                      Long id,

                                  @RequestPart(value = "posterFile",required = false)
                                      @Nullable
                                      @Parameter(description = "电影海报封面")
                                      MultipartFile posterFile,

                                  @RequestParam("name")
                                      @Nullable
                                      @Parameter(description = "电影名")
                                      String name,

                                  @RequestParam("duration")
                                      @Nullable
                                      @Parameter(description = "电影时长（分钟）")
                                      Integer duration,

                                  @RequestParam("release")
                                      @Nullable
                                      @DateTimeFormat(pattern = "yyyy-MM-dd")
                                      @Parameter(description = "上映时间")
                                      LocalDate release,

                                  @RequestParam("tags")
                                      @Nullable
                                      @Parameter(description = "电影类别")
                                      List<Long> tags,

                                  @RequestParam("language" )
                                      @Nullable
                                      @Parameter(description = "语种")
                                      Language language,

                                  @RequestParam("source")
                                      @Nullable
                                      @Parameter(description = "片源（国家）")
                                      String source){
        return movieService.editMovie(id,name,duration,release,tags,language,source,posterFile);
    }
    @DeleteMapping("/movie")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    @Operation(summary = "删除电影(软删除)")
    public ApiResult<?> deleteMovie(@RequestParam("id") Long movieId){
        return movieService.deleteMovie(movieId);
    }

    @GetMapping("/movies")
    @Operation(summary = "获取所有电影")
    public ApiResult<Page<MovieBriefInfo>> getAllMovies(@RequestParam("currentPage")Integer currentPage,
                                               @RequestParam("pageSize")Integer pageSize){

        return movieService.getAllMovies(currentPage,pageSize);
    }

    @GetMapping("/movies/details/all")
    @Operation(summary = "获取所有电影详细信息")
    public ApiResult<List<MovieDTO>> getAllMoviesDetails(){

        return movieService.getAllMoviesDetails();
    }

    @GetMapping("/cinema/{cinema_id}/nowShowings")
    @Operation(summary = "根据影院查询正在上映电影")
    public ApiResult<?> getShowingMoviesByCinema(@PathVariable("cinema_id")Long cinemaId){
        return ticketService.getNowShowingMovieByCinema(cinemaId);
    }

    @GetMapping("/movie/search")
    @Operation(summary = "搜索电影")
    public ApiResult<Page<MovieBriefInfo>> searchMovies(@RequestParam("currentPage")Integer currentPage,
                                                        @RequestParam("pageSize")Integer pageSize,
                                                        @RequestParam("name")@Parameter(required = false) String name){
        return movieService.queryMovies(currentPage,pageSize,name);
    }

    @GetMapping("movies/deleted")
    @Operation(summary = "获取已删除的电影")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<Page<Movie>> getDeletedMovies(@RequestParam("currentPage")Integer currentPage,
                                                   @RequestParam("pageSize")Integer pageSize){
        return movieService.getDeletedMovies(currentPage,pageSize);
    }

    @PostMapping("movie/undelete")
    @Operation(summary = "恢复已删除的电影")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> UndeletedMovies(@RequestParam("id") Long id){
        return movieService.undeletedMovies(id);
    }

    @DeleteMapping("movie/permanentDelete")
    @Operation(summary = "永久删除电影")
    @CheckLogin
    @SaCheckRole(mode = SaMode.OR,value = {"STAFF","ADMIN"})
    public ApiResult<?> permanentDeleteMovie(@RequestParam("id") Long id){
        return movieService.permanentDeleteMovie(id);
    }

    public MovieController(MovieService movieService,
                           TicketService ticketService) {
        this.movieService = movieService;
        this.ticketService = ticketService;
    }
}
