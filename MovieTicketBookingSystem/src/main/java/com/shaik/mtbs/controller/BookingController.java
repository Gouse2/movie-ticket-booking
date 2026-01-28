package com.shaik.mtbs.controller;

import com.shaik.mtbs.dto.BookingDTO;
import com.shaik.mtbs.entity.Booking;
import com.shaik.mtbs.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking) {
        Booking savedBooking = bookingService.createBooking(booking);
        return ResponseEntity.ok(savedBooking);
    }

    // New: Get all bookings
    @GetMapping
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        return bookings.stream()
                       .map(BookingDTO::new)
                       .toList();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok("Booking cancelled successfully. Seats are now available again.");
    }
    
}