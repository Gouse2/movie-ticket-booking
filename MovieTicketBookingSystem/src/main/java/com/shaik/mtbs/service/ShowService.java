package com.shaik.mtbs.service;


import com.shaik.mtbs.entity.Seat;
import com.shaik.mtbs.entity.Show;
import com.shaik.mtbs.repository.SeatRepository;
import com.shaik.mtbs.repository.ShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowService {

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private SeatRepository seatRepository;      // ← Very important !!

    // Get all shows
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

    // Get shows of particular movie
    public List<Show> getShowsByMovieId(Long movieId) {
        return showRepository.findByMovieId(movieId);
    }

    // Most important → Add show + automatically create seats
    public Show addShow(Show show) {
        Show savedShow = showRepository.save(show);

        List<Seat> seats = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 10; col++) {
                Seat seat = new Seat();
                seat.setShow(savedShow);
                seat.setRow(row);
                seat.setColumn(col);
                seat.setStatus(Seat.SeatStatus.AVAILABLE);

                double base = show.getBaseTicketPrice();
                double price = (row <= 3) ? base * 1.2 : (row <= 6) ? base : base * 0.8;
                seat.setPrice(price);

                seats.add(seat);
            }
        }

        System.out.println("Creating " + seats.size() + " seats for show ID: " + savedShow.getId());

        seatRepository.saveAll(seats);

        System.out.println("Seats saved to database!");

        savedShow.setSeats(seats);
        return savedShow;
    }
}