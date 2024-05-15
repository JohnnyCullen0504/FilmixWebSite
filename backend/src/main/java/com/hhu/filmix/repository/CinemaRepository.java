package com.hhu.filmix.repository;

import com.hhu.filmix.api.Page;
import com.hhu.filmix.entity.Cinema;

import java.util.Optional;
import java.util.List;

public interface CinemaRepository {
    public Optional<Cinema> findCinemaById(Long id);
    public Long insertCinema(Cinema cinema);
    public void updateCinema(Cinema cinema);
    public void deleteCinemaById(Long id);
    void deprecatedCinema(Long id);

    Page<Cinema> findAllCinemas(Integer currentPage, Integer pageSize);

    Page<Cinema> findCinemasByName(Integer currentPage, Integer pageSize, String name);

    Page<Cinema> findDeletedCinema(Integer currentPage, Integer pageSize);

    Optional<Cinema> findDeletedCinemaById(Long id);

    void undeleteCinema(Long id);

    void permanentDeleteCinema(Long id);

    List<Cinema> findByNameEqual(String name);

    List<Cinema> findByAddressEqual(String address);
}
