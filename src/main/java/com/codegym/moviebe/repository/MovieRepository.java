package com.codegym.moviebe.repository;

import com.codegym.moviebe.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}


