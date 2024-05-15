package com.hhu.filmix.repository;

import com.hhu.filmix.entity.MovieTag;

import java.util.List;
import java.util.Optional;

public interface MovieTagRepository {
    Optional<MovieTag> findTagById(Long id);
    Long insertTag(MovieTag tag);
    Long insertMovieAndTagRelationShip(Long movieId,Long tagId);
    void clearRelationShipForMovie(Long movieId);
    void clearRelationShipForTag(Long tagId);
    void deleteTag(Long tagId);

    List<MovieTag> getAllTages();

    List<MovieTag> findMovieTags(Long id);
}
