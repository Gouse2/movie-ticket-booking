package com.shaik.mtbs.repository;

import com.shaik.mtbs.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShowRepository extends JpaRepository<Show, Long> {

    // Custom query to find shows by movie ID
    @Query("SELECT s FROM Show s WHERE s.movie.id = :movieId")
    List<Show> findByMovieId(@Param("movieId") Long movieId);
}