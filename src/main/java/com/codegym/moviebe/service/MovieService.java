package com.codegym.moviebe.service;

import com.codegym.moviebe.model.Movie;

import java.util.List;
import java.util.Optional;

public interface MovieService {
    List<Movie> getAll();
    Optional<Movie> getById(Long id);
}


