package com.codegym.moviebe.controller;

import com.codegym.moviebe.entity.Movie;
import com.codegym.moviebe.entity.Showtime;
import com.codegym.moviebe.repository.MovieRepository;
import com.codegym.moviebe.repository.ShowtimeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    public AdminController(MovieRepository movieRepository, ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    // Movies
    @GetMapping("/movies")
    public List<Movie> listMovies() { return movieRepository.findAll(); }

    @PostMapping("/movies")
    public Movie createMovie(@RequestBody Movie m) { return movieRepository.save(m); }

    @PutMapping("/movies/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie m) {
        return movieRepository.findById(id)
                .map(old -> { m.setId(old.getId()); return ResponseEntity.ok(movieRepository.save(m)); })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        if (!movieRepository.existsById(id)) return ResponseEntity.notFound().build();
        movieRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Showtimes
    @GetMapping("/movies/{movieId}/showtimes")
    public List<Showtime> listShowtimes(@PathVariable Long movieId) { return showtimeRepository.findByMovieId(movieId); }

    @PostMapping("/movies/{movieId}/showtimes")
    public Showtime createShowtime(@PathVariable Long movieId, @RequestBody Showtime st) {
        st.setMovieId(movieId);
        return showtimeRepository.save(st);
    }

    @PutMapping("/showtimes/{id}")
    public ResponseEntity<Showtime> updateShowtime(@PathVariable Long id, @RequestBody Showtime st) {
        return showtimeRepository.findById(id)
                .map(old -> { st.setId(old.getId()); st.setMovieId(old.getMovieId()); return ResponseEntity.ok(showtimeRepository.save(st)); })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/showtimes/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        if (!showtimeRepository.existsById(id)) return ResponseEntity.notFound().build();
        showtimeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


