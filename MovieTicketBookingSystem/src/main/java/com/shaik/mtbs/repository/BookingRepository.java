package com.shaik.mtbs.repository;
 
import com.shaik.mtbs.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}