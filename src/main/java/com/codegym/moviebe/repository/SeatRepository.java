package com.codegym.moviebe.repository;

import com.codegym.moviebe.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {
    List<Seat> findByShowtimeId(Long showtimeId);
    boolean existsByShowtimeIdAndSeatCode(Long showtimeId, String seatCode);
}


