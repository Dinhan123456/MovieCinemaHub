package com.codegym.moviebe.repository;

import com.codegym.moviebe.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}


