package com.hhu.filmix.service.impl;

import com.hhu.filmix.api.ApiCode;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.MovieTagDTO.response.MovieTagDTO;
import com.hhu.filmix.dto.PutResponseDTO;
import com.hhu.filmix.dto.cinemaDTO.response.CinemaTicketDTO;
import com.hhu.filmix.dto.movieDTO.response.MovieBriefInfo;
import com.hhu.filmix.dto.movieDTO.response.MovieDTO;
import com.hhu.filmix.dto.ticketDTO.request.NewTicketRequest;
import com.hhu.filmix.dto.ticketDTO.response.TicketBriefDTO;
import com.hhu.filmix.dto.ticketDTO.response.TicketDetailDTO;
import com.hhu.filmix.dto.ticketDTO.serviceTransfer.TicketDetail;
import com.hhu.filmix.dtoMapper.*;
import com.hhu.filmix.entity.*;
import com.hhu.filmix.repository.*;
import com.hhu.filmix.service.ObjectStorageService;
import com.hhu.filmix.service.TicketService;
import com.hhu.filmix.util.Bucket;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository;
    private MovieRepository movieRepository;
    private CinemaRepository cinemaRepository;
    private RoomRepository roomRepository;
    public CinemaDTOMapper cinemaDTOMapper;
    public TicketDTOMapper ticketDTOMapper;
    public MovieDTOMapper movieDTOMapper;
    private RoomDTOMapper roomDTOMapper;
    private MovieTagDTOMapper movieTagDTOMapper;
    private MovieTagRepository movieTagRepository;
    private ObjectStorageService objectStorageService;
    private PurchaseRepository purchaseRepository;
    private PurchaseSeatRepository purchaseSeatRepository;
    @Override
    /*
    * 获取单日的电影票信息
    * 1.通过电影id和日期查找当日的电影排期（电影票）
    *   a.若查询日期为今天，则会自动转换为从现在开始往后的电影排期
    * 2.将查到的电影按照门店归类
    * 3.将电影排期封装为DTO(门店为单位)以列表型式返回
    *   a.cinemaDTO对象使用DTOMapper自动映射
    *   b.ticketBriefInfo对象使用lambda表达式手动映射
    * */
    public ApiResult<List<CinemaTicketDTO>> queryMovieTickets(Long movieId, LocalDate queryDate) {
        //检查电影是否存在
        if(movieRepository.findMovieById(movieId).isEmpty()){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影不存在");
        }

        LocalDateTime queryDateTime = queryDate.atStartOfDay();
        //若日期为今天，则筛选时间精确到小时
        if(queryDate.isEqual(LocalDate.now())){
            queryDateTime = LocalDateTime.now();
        }
        LocalDateTime nextDate= queryDateTime.plusDays(1).toLocalDate().atStartOfDay();
        List<TicketDetail> movieDetailList = ticketRepository.findByMovieAndDateBetween(movieId, queryDateTime, nextDate);

        Map<Cinema,List<TicketDetail>> Cinema2TicketListMap =
                movieDetailList.stream().collect(Collectors.groupingBy(TicketDetail::cinema));
        List<CinemaTicketDTO> cinemaTicketDTOList = new ArrayList<>();

        Cinema2TicketListMap
                .entrySet()
                .stream()
                .forEach(entry->
                        cinemaTicketDTOList.add(new CinemaTicketDTO(
                        cinemaDTOMapper.toCinemaDTO(entry.getKey()),//movie
                        entry.getValue().stream().map(ticketDetail -> new TicketBriefDTO(//movieTimeDTOList
                                ticketDetail.id(),
                                ticketDetail.movie().getId(),
                                ticketDetail.showTime(),
                                ticketDetail.endTime(),
                                ticketDetail.cinema().getId(),
                                ticketDetail.roomId(),
                                ticketDetail.price(),
                                ticketDetail.canceled()
                        )).collect(Collectors.toList()))));
        return ApiResult.data(cinemaTicketDTOList);
    }

    @Override
    public ApiResult<?> queryShowDate(Long movieId) {
        //检查电影是否存在
        if(movieRepository.findMovieById(movieId).isEmpty()){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影不存在");
        }

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<String> showDates =
                ticketRepository.findShowDateByMovie(movieId)
                        .stream()
                        .sorted()
                        .map(localDate -> localDate.format(fmt))
                        .toList();
        return ApiResult.data(showDates);
    }

    @Override
    public ApiResult<TicketDetailDTO> getTicketFullInfo(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if(ticket == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影排期不存在");
        }
        Cinema cinema = cinemaRepository.findCinemaById(ticket.getCinemaId()).orElse(null);
        if(cinema == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"影院不存在");
        }
        Room room =  roomRepository.findRoomById(ticket.getRoomId()).orElse(null);
        if(room == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"影厅不存在");
        }
        Movie movie = movieRepository.findMovieById(ticket.getMovieId()).orElse(null);
        if(movie == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"电影不存在");
        }
        List<MovieTagDTO> movieTagDTOS = movieTagRepository.
                findMovieTags(movie.getId())
                .stream()
                .map(movieTag -> movieTagDTOMapper.toMovieTagDTO(movieTag))
                .collect(Collectors.toList());

        //查询已购买座位
        List<PurchaseSeat> purchaseSeats = purchaseSeatRepository.findValidSeatByTicketId(ticketId);
        List<String> soldInfo = purchaseSeats
                .stream()
                .map(purchaseSeat -> {
                    String coordinate = "";
                    coordinate += purchaseSeat.getRow();
                    coordinate += ",";
                    coordinate += purchaseSeat.getColumn();
                    return coordinate;
                })
                .collect(Collectors.toList());

        MovieDTO movieDTO = movieDTOMapper.toMovieDTO(movie,movieTagDTOS);
        //生成海报url地址
        movieDTO.setPosterURL(objectStorageService.getObjectURL(Bucket.POSTER,movieDTO.getPosterURL()));
        return ApiResult.data("排期信息查询成功",new TicketDetailDTO(
                ticket.getId(),
                ticket.getShowTime(),
                ticket.getEndTime(),
                movieDTO,
                cinemaDTOMapper.toCinemaDTO(cinema),
                roomDTOMapper.toRoomDTO(room),
                ticket.getPrice(),
                soldInfo,
                ticket.getCanceled()));
    }

    @Override
    public ApiResult<?> newTicket(NewTicketRequest newTicketRequest) {
        Ticket newTicket = ticketDTOMapper.toTicket(newTicketRequest);
        newTicket.setCanceled(false);
        if(movieRepository.findMovieById(newTicket.getMovieId()).isEmpty())
            return ApiResult.fail(ApiCode.NOTFOUND,"所属电影不存在");
        if(cinemaRepository.findCinemaById(newTicket.getCinemaId()).isEmpty())
            return ApiResult.fail(ApiCode.NOTFOUND,"所属影院不存在");
        if(roomRepository.findRoomById(newTicket.getRoomId()).isEmpty())
            return ApiResult.fail(ApiCode.NOTFOUND,"所属影厅不存在");

        //影厅的时间冲突检测
        Ticket ticket = ticketRepository.checkRoomTimeConflict(
                newTicketRequest.getRoomId(),
                newTicketRequest.getShowTime(),
                newTicketRequest.getEndTime()).orElse(null);
        if(ticket!=null){
            return ApiResult.fail(ApiCode.RESOURCE_CONFLICT,"影厅在该时段已有排期",ticketDTOMapper.toTicketBriefDTO(ticket));
        }
        Long ticketId =ticketRepository.insertTicket(newTicket);
        return ApiResult.data("新建排期成功", PutResponseDTO.phrase(ticketId));
    }

    @Override
    @Transactional
    public ApiResult<?> cancelTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElse(null);
        if(ticket == null){
            return ApiResult.fail(ApiCode.NOTFOUND,"该电影排期不存在");
        }
        if(ticket.getShowTime().isBefore(LocalDateTime.now())){
            return ApiResult.fail(ApiCode.UNABLE,"已过放映时间的电影无法取消");
        }
        //取消电影排期
        ticket.setCanceled(true);
        ticketRepository.updateMovieTie(ticket);
        return ApiResult.success("电影排期取消成功");
        //TODO:待退款订单
    }

    @Override
    public ApiResult<List<MovieBriefInfo>> nowShowingMovie() {
        List<Movie> movies = movieRepository.getNowShowingMovie(LocalDateTime.now());
        return ApiResult.data(movies
                .stream()
                .map(movie -> {
                    MovieBriefInfo briefInfo = movieDTOMapper.toMovieBriefInfo(movie);
                    briefInfo.setPosterURL(objectStorageService.getObjectURL(Bucket.POSTER,briefInfo.getPosterURL()));
                    return briefInfo;})
                .collect(Collectors.toList()));
    }

    @Override
    public ApiResult<List<MovieBriefInfo>> queryUpComingMovies() {
        List<Movie> movies = movieRepository.getUpComingMovie(LocalDateTime.now());
        return ApiResult.data(movies
                .stream()
                .map(movie -> {
                    MovieBriefInfo briefInfo = movieDTOMapper.toMovieBriefInfo(movie);
                    briefInfo.setPosterURL(objectStorageService.getObjectURL(Bucket.POSTER,briefInfo.getPosterURL()));
                    return briefInfo;})
                .collect(Collectors.toList()));
    }

    @Override
    public ApiResult<?> getNowShowingMovieByCinema(Long cinemaId) {
        List<Movie> movies = Collections.synchronizedList(movieRepository.getNowShowingMovieByCinema(cinemaId));
        List<MovieDTO> movieDTOS = movies.parallelStream()
                .map(
                movie -> {

                    MovieDTO md = movieDTOMapper.toMovieDTO(
                            movie,
                            movieTagRepository
                                    .findMovieTags(movie.getId())
                                    .stream()
                                    .map(mt->movieTagDTOMapper.toMovieTagDTO(mt))
                                    .collect(Collectors.toList()));
                    md.setPosterURL(objectStorageService.getObjectURL(Bucket.POSTER,movie.getPosterURL()));
                    return md;
                })
                .collect(Collectors.toList());
        return ApiResult.data(movieDTOS);
    }

    public TicketServiceImpl(TicketRepository ticketRepository,
                             CinemaDTOMapper cinemaDTOMapper,
                             MovieRepository movieRepository,
                             TicketDTOMapper ticketDTOMapper,
                             CinemaRepository cinemaRepository,
                             RoomRepository roomRepository,
                             MovieDTOMapper movieDTOMapper,
                             RoomDTOMapper roomDTOMapper,
                             MovieTagDTOMapper movieTagDTOMapper,
                             MovieTagRepository movieTagRepository,
                             ObjectStorageService objectStorageService,
                             PurchaseRepository purchaseRepository,
                             PurchaseSeatRepository purchaseSeatRepository) {
        this.ticketRepository = ticketRepository;
        this.cinemaDTOMapper = cinemaDTOMapper;
        this.movieRepository = movieRepository;
        this.ticketDTOMapper = ticketDTOMapper;
        this.cinemaRepository = cinemaRepository;
        this.roomRepository = roomRepository;
        this.movieDTOMapper = movieDTOMapper;
        this.roomDTOMapper = roomDTOMapper;
        this.movieTagDTOMapper = movieTagDTOMapper;
        this.movieTagRepository = movieTagRepository;
        this.objectStorageService = objectStorageService;
        this.purchaseRepository = purchaseRepository;
        this.purchaseSeatRepository = purchaseSeatRepository;
    }
}
