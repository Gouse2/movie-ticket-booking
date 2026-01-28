package com.shaik.mtbs.service;

import com.shaik.mtbs.entity.Seat;
import com.shaik.mtbs.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatRepository seatRepository;

    public List<Seat> getSeatsForShow(Long showId) {
        return seatRepository.findByShowId(showId);
    }

    public Seat updateSeat(Seat seat) {
        return seatRepository.save(seat);
    }
}