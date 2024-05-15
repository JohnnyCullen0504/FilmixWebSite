package com.hhu.filmix.service.impl;

import cn.hutool.core.util.IdUtil;
import com.hhu.filmix.api.ApiCode;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.api.Page;
import com.hhu.filmix.dto.MovieTagDTO.response.MovieTagDTO;
import com.hhu.filmix.dto.PutResponseDTO;
import com.hhu.filmix.dto.movieDTO.response.MovieBriefInfo;
import com.hhu.filmix.dto.movieDTO.response.MovieDTO;
import com.hhu.filmix.dtoMapper.MovieDTOMapper;
import com.hhu.filmix.dtoMapper.MovieTagDTOMapper;
import com.hhu.filmix.entity.Movie;
import com.hhu.filmix.enumeration.Language;
import com.hhu.filmix.repository.MovieRepository;
import com.hhu.filmix.repository.MovieTagRepository;
import com.hhu.filmix.service.MovieService;
import com.hhu.filmix.service.ObjectStorageService;
import com.hhu.filmix.util.Bucket;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieServicesImpl implements MovieService {
    private MovieRepository movieRepository;
    private MovieDTOMapper movieDTOMapper;
    private ObjectStorageService objectStorageService;
    private MovieTagRepository movieTagRepository;
    private MovieTagDTOMapper movieTagDTOMapper;
    @Override
    public ApiResult<MovieDTO> getMovieInfo(Long movieId) {
        Movie movie = movieRepository.findMovieById(movieId).orElse(null);
        if (movie == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影不存在");
        }
         List<MovieTagDTO> movieTagDTOS = movieTagRepository.
                 findMovieTags(movieId)
                 .stream()
                 .map(movieTag -> movieTagDTOMapper.toMovieTagDTO(movieTag))
                 .collect(Collectors.toList());

        //构造图片url参数
        MovieDTO movieDTO = movieDTOMapper.toMovieDTO(movie,movieTagDTOS);
        movieDTO.setPosterURL(objectStorageService.getObjectURL(Bucket.POSTER,movieDTO.getPosterURL()));
         return ApiResult.data("获取电影成功",movieDTO);
    }

    @SneakyThrows
    @Override
    @Transactional
    public ApiResult<?> createNewMovie(String name, Integer duration, LocalDate release, List<Long> tags, Language language, String source, MultipartFile posterFile) {
        //判断是否有重名电影
        List<Movie> movies = movieRepository.findMovieByNameEqual(name);
        if(!movies.isEmpty())
            return ApiResult.fail(ApiCode.RESOURCE_CONFLICT,"已有重名电影");

        //上传电影海报
        String suffix = posterFile.getOriginalFilename().substring(posterFile.getOriginalFilename().lastIndexOf("."));//文件后缀名
        String posterObjectName = IdUtil.simpleUUID() + suffix ;
        objectStorageService.uploadObject(Bucket.POSTER,posterObjectName,posterFile.getInputStream());
        Movie movie = new Movie(null,
                name,
                duration,
                release,
                posterObjectName,
                language,
                source,
                false);
        Long movieId = movieRepository.insertMovie(movie);
        //检查标签合法性(事务仅在抛出异常才会回滚，为了降低代码复杂度，使用两次循环)
        for(Long tagId : tags){
            if(movieTagRepository.findTagById(tagId).isEmpty())
                return ApiResult.fail(ApiCode.NOTFOUND,"id为" + tagId + "的标签不存在");
        }
        for(Long tagId:tags){
            movieTagRepository.insertMovieAndTagRelationShip(movieId,tagId);
        }
        return ApiResult.data("新增电影成功", PutResponseDTO.phrase(movieId));
    }

    @SneakyThrows
    @Override
    public ApiResult<?> editMovie(Long id, String name, Integer duration, LocalDate release, List<Long> tags, Language language, String source, MultipartFile posterFile) {
       Movie movie = movieRepository.findMovieById(id).orElse(null);
       if (movie == null){
           return ApiResult.fail(ApiCode.NOTFOUND,"该电影不存在");
       }
       if(name!=null&& !name.isEmpty())
       {
           Movie sameNameMovie = movieRepository.findMovieByNameEqual(name).stream().findFirst().orElse(null);
           if(sameNameMovie != null && !sameNameMovie.getId().equals(id))
               return ApiResult.fail(ApiCode.RESOURCE_CONFLICT,"所修改名字与已有电影重名");
           movie.setName(name);
       }

       if (duration!=null)
           movie.setDuration(duration);
       if (release!=null)
           movie.setReleaseTime(release);
       if (tags!=null) {
           //清除标签关系
           movieTagRepository.clearRelationShipForMovie(id);
           //检查标签合法性(事务仅在抛出异常才会回滚，为了降低代码复杂度，使用两次循环)
           for(Long tagId : tags){
               if(movieTagRepository.findTagById(tagId).isEmpty())
                   return ApiResult.fail(ApiCode.NOTFOUND,"id为" + tagId + "的标签不存在");
           }
           for(Long tagId:tags){
               movieTagRepository.insertMovieAndTagRelationShip(id,tagId);
           }
       }
       if (language!=null)
           movie.setLanguage(language);
       if (source!=null && !source.isEmpty())
           movie.setSource(source);
       if (posterFile != null && !posterFile.isEmpty()){
           //上传电影海报
           String suffix = posterFile.getOriginalFilename().substring(posterFile.getOriginalFilename().lastIndexOf("."));//文件后缀名
           String posterObjectName = IdUtil.simpleUUID() + suffix ;
           objectStorageService.uploadObject(Bucket.POSTER,posterObjectName,posterFile.getInputStream());
           movie.setPosterURL(posterObjectName);
       }
           movieRepository.updateMovie(movie);
        return ApiResult.success("更新成功");
    }

    @Override
    public ApiResult<?> deleteMovie(Long movieId) {
        if(movieRepository.findMovieById(movieId).isEmpty()){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影不存在");
        }
        movieRepository.deleteMovie(movieId);
        return ApiResult.success("电影删除成功");
    }

    @Override
    public ApiResult<Page<MovieBriefInfo>> getAllMovies(Integer currentPage, Integer pageSize) {
        Page<MovieBriefInfo> movieBriefInfos;
        Page<Movie> movies=  movieRepository.findALlMovies(currentPage,pageSize);
        movieBriefInfos = Page.<MovieBriefInfo>builder()
                .pageSize(pageSize)
                .currentPage(currentPage)
                .totalPage(movies.getTotalPage())
                .records(movies.getRecords().stream().map(movie -> {
                    MovieBriefInfo mb = movieDTOMapper.toMovieBriefInfo(movie);
                    mb.setPosterURL(objectStorageService.getObjectURL(Bucket.POSTER, mb.getPosterURL()));
                    return mb;
                }).collect(Collectors.toList()))
                .build();

        return ApiResult.data(movieBriefInfos);
    }

    @Override
    public ApiResult<Page<MovieBriefInfo>> queryMovies(Integer currentPage, Integer pageSize, String name) {
        Page<Movie> movies =  movieRepository.queryMovies(currentPage,pageSize,name);
        Page<MovieBriefInfo> movieBriefInfos;
        movieBriefInfos = Page.<MovieBriefInfo>builder()
                .pageSize(pageSize)
                .currentPage(currentPage)
                .totalPage(movies.getTotalPage())
                .records(movies.getRecords().stream().map(movie -> {
                    MovieBriefInfo mb = movieDTOMapper.toMovieBriefInfo(movie);
                    mb.setPosterURL(objectStorageService.getObjectURL(Bucket.POSTER, mb.getPosterURL()));
                    return mb;
                }).collect(Collectors.toList()))
                .build();

        return ApiResult.data(movieBriefInfos);
    }

    @Override
    public ApiResult<Page<Movie>> getDeletedMovies(Integer currentPage, Integer pageSize) {
        Page<Movie> moviePage = movieRepository.findDeletedMovies(currentPage,pageSize);
        return ApiResult.data(moviePage);
    }

    @Override
    public ApiResult<?> undeletedMovies(Long id) {
        Movie movie = movieRepository.findDeletedMovieById(id).orElse(null);
        if (movie == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影未被(软)删除");
        }
        movieRepository.undeleteMovie(id);
        return ApiResult.success("电影恢复成功");
    }

    @Override
    @Transactional
    public ApiResult<?> permanentDeleteMovie(Long id) {
        Movie movie = movieRepository.findDeletedMovieById(id).orElse(null);
        if (movie == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影未被(软)删除");
        }
        movieRepository.permanentDeleteMovie(id);
        movieTagRepository.clearRelationShipForMovie(id);
        //不删除排期和订单，但是由于数据库的数据不完整性，订单查询会无法查询到电影信息，所以数据推荐使用软删除
        return ApiResult.success("该电影已被永久删除");
    }

    @Override
    public ApiResult<List<MovieDTO>> getAllMoviesDetails() {

        List<Movie> movies  = Collections.synchronizedList(movieRepository.findALlMovies());
        List<MovieDTO> movieDTOS = movies.parallelStream().map(movie -> {
            List<MovieTagDTO> movieTagDTOS = movieTagRepository.findMovieTags(movie.getId())
                    .stream()
                    .map(movieTag -> movieTagDTOMapper.toMovieTagDTO(movieTag))
                    .collect(Collectors.toList());

            //构造图片url参数
            MovieDTO movieDTO = movieDTOMapper.toMovieDTO(movie,movieTagDTOS);
            movieDTO.setPosterURL(objectStorageService.getObjectURL(Bucket.POSTER,movieDTO.getPosterURL()));
            return movieDTO;
        }).collect(Collectors.toList());
        return ApiResult.data(movieDTOS);

    }

    public MovieServicesImpl(MovieRepository movieRepository,
                             MovieDTOMapper movieDTOMapper,
                             ObjectStorageService objectStorageService,
                             MovieTagRepository movieTagRepository,
                             MovieTagDTOMapper movieTagDTOMapper) {
        this.movieRepository = movieRepository;
        this.movieDTOMapper = movieDTOMapper;
        this.objectStorageService = objectStorageService;
        this.movieTagRepository = movieTagRepository;
        this.movieTagDTOMapper = movieTagDTOMapper;

    }
}
