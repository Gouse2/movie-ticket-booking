package com.shaik.mtbs.service;

import com.shaik.mtbs.entity.Booking;
import com.shaik.mtbs.entity.Seat;
import com.shaik.mtbs.repository.BookingRepository;
import com.shaik.mtbs.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private SeatRepository seatRepository;

    public Booking createBooking(Booking booking) {
        // Calculate total amount
        double total = 0.0;
        List<Seat> bookedSeats = booking.getBookedSeats();

        for (Seat seat : bookedSeats) {
            // Fetch fresh seat from DB to avoid stale data
            Seat dbSeat = seatRepository.findById(seat.getId())
                    .orElseThrow(() -> new RuntimeException("Seat not found: " + seat.getId()));

            if (dbSeat.getStatus() != Seat.SeatStatus.AVAILABLE) {
                throw new RuntimeException("Seat already booked: Row " + dbSeat.getRow() + ", Col " + dbSeat.getColumn());
            }

            dbSeat.setStatus(Seat.SeatStatus.BOOKED);
            seatRepository.save(dbSeat);

            total += dbSeat.getPrice();
        }

        // Add 18% GST (simple example)
        double finalTotal = total * 1.18;
        booking.setTotalAmount(finalTotal);

        // Save the booking
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
    
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with ID: " + bookingId));

        // Mark all seats back to AVAILABLE
        for (Seat seat : booking.getBookedSeats()) {
            seat.setStatus(Seat.SeatStatus.AVAILABLE);
            seatRepository.save(seat);
        }

        // Delete the booking
        bookingRepository.delete(booking);
    }
    
}