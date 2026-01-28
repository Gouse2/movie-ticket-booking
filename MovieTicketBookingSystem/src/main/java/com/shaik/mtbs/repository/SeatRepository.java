package com.shaik.mtbs.repository;

import com.shaik.mtbs.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT s FROM Seat s WHERE s.show.id = :showId")
    List<Seat> findByShowId(@Param("showId") Long showId);
}