package com.hhu.filmix.RepositoryTest;

import com.hhu.filmix.FilmixBackendApplication;
import com.hhu.filmix.api.ApiResult;
import com.hhu.filmix.dto.PutResponseDTO;
import com.hhu.filmix.dto.cinemaDTO.request.CinemaRegisterRequest;
import com.hhu.filmix.entity.Cinema;
import com.hhu.filmix.repository.CinemaRepository;
import com.hhu.filmix.service.CinemaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {FilmixBackendApplication.class})
@Slf4j
public class CinemaTest {
    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private CinemaRepository cinemaRepository;

    @Test
    public void CinemaPersistentAndDelete(){
        CinemaRegisterRequest request = new CinemaRegisterRequest();
        request.setName("test-cinema");
        request.setAddress("test-address");
        ApiResult<PutResponseDTO> result = (ApiResult<PutResponseDTO>) cinemaService.registerCinema(request);
        Long id =  result.getData().getId();
        Cinema cinema = cinemaRepository.findCinemaById(id).orElse(null);

        //Assert
        Assertions.assertEquals(cinema.getId(),id);
        Assertions.assertEquals(cinema.getAddress(),request.getAddress());
        Assertions.assertEquals(cinema.getName(),request.getName());

        //Delete cinema
        cinemaRepository.deleteCinemaById(id);
        cinema = cinemaRepository.findCinemaById(id).orElse(null);
        Assertions.assertNull(cinema);
    }
}
