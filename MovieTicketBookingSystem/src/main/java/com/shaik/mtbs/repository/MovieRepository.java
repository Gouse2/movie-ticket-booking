package com.shaik.mtbs.repository;

import com.shaik.mtbs.entity.Movie; 
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}