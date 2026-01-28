package com.shaik.mtbs.controller;

import com.shaik.mtbs.entity.Seat;
import com.shaik.mtbs.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seats")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @GetMapping("/show/{showId}")
    public List<Seat> getSeatsForShow(@PathVariable Long showId) {
        return seatService.getSeatsForShow(showId);
    }
}