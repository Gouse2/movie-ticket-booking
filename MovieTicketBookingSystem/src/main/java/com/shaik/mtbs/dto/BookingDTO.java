package com.shaik.mtbs.dto;

import com.shaik.mtbs.entity.Booking;
import com.shaik.mtbs.entity.Seat;

import java.util.List;
import java.util.stream.Collectors;

public class BookingDTO {

    private Long id;
    private String userName;
    private String userPhone;
    private Long showId;
    private String showTime;          // Added for display
    private int showScreenNumber;     // Added for display
    private String movieTitle;        // Optional - movie name
    private double totalAmount;
    private List<SeatDTO> bookedSeats;

    // Important constructor for stream.map(BookingDTO::new)
    public BookingDTO(Booking booking) {
        this.id = booking.getId();
        this.userName = booking.getUserName();
        this.userPhone = booking.getUserPhone();
        this.totalAmount = booking.getTotalAmount();

        // Copy show details safely
        if (booking.getShow() != null) {
            this.showId = booking.getShow().getId();
            this.showTime = booking.getShow().getTime();
            this.showScreenNumber = booking.getShow().getScreenNumber();

            // Optional: movie title
            if (booking.getShow().getMovie() != null) {
                this.movieTitle = booking.getShow().getMovie().getTitle();
            }
        }

        // Map seats to SeatDTO
        this.bookedSeats = booking.getBookedSeats().stream()
                .map(seat -> new SeatDTO(
                        seat.getId(),
                        seat.getRow(),
                        seat.getColumn(),
                        seat.getStatus().name(),
                        seat.getPrice()
                ))
                .collect(Collectors.toList());
    }

    // All getters (required for Jackson to serialize to JSON)
    public Long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public Long getShowId() {
        return showId;
    }

    public String getShowTime() {
        return showTime;
    }

    public int getShowScreenNumber() {
        return showScreenNumber;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public List<SeatDTO> getBookedSeats() {
        return bookedSeats;
    }

    // Optional: setters if needed (not required for read-only DTO)
    // You can omit setters if this DTO is only for response
}