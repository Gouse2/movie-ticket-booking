package com.shaik.mtbs.controller;

import com.shaik.mtbs.entity.Show;
import com.shaik.mtbs.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping
    public List<Show> getAllShows() {
        return showService.getAllShows();
    }

    // New: Get shows for a specific movie
    @GetMapping("/movie/{movieId}")
    public List<Show> getShowsByMovie(@PathVariable Long movieId) {
        return showService.getShowsByMovieId(movieId);
    }

    @PostMapping
    public ResponseEntity<Show> addShow(@RequestBody Show show) {
        Show savedShow = showService.addShow(show);
        return ResponseEntity.ok(savedShow);
    }
}