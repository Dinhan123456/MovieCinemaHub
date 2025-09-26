package com.codegym.moviebe.controller;

import com.codegym.moviebe.entity.Booking;
import com.codegym.moviebe.entity.Movie;
import com.codegym.moviebe.entity.Seat;
import com.codegym.moviebe.entity.Showtime;
import com.codegym.moviebe.model.BookingRequest;
import com.codegym.moviebe.repository.BookingRepository;
import com.codegym.moviebe.repository.MovieRepository;
import com.codegym.moviebe.repository.SeatRepository;
import com.codegym.moviebe.repository.ShowtimeRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, allowCredentials = "true")
public class MovieController {

    private final MovieRepository movieRepository;
    private final BookingRepository bookingRepository;
    private final ShowtimeRepository showtimeRepository;
    private final SeatRepository seatRepository;

    public MovieController(MovieRepository movieRepository, BookingRepository bookingRepository, ShowtimeRepository showtimeRepository, SeatRepository seatRepository) {
        this.movieRepository = movieRepository;
        this.bookingRepository = bookingRepository;
        this.showtimeRepository = showtimeRepository;
        this.seatRepository = seatRepository;
    }

    @GetMapping("/movies")
    public List<Movie> listMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<Movie> getMovie(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/movies/{id}/showtimes")
    public List<Showtime> getShowtimes(@PathVariable Long id) {
        return showtimeRepository.findByMovieId(id);
    }

    @GetMapping("/showtimes/{id}/seats")
    public List<Seat> getSeats(@PathVariable Long id) {
        return seatRepository.findByShowtimeId(id);
    }

    @PostMapping("/bookings")
    public ResponseEntity<Map<String, Object>> createBooking(@Valid @RequestBody BookingRequest request) {
        // Basic seat check + mark sold
        if (request.getSeats() == null || request.getSeats().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "No seats"));
        }
        Long movieId = request.getMovieId();
        String code = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        // Mark seats sold for provided showtimeId
        Long stId = request.getShowtimeId();
        if (stId == null) {
            List<Showtime> sts = showtimeRepository.findByMovieId(movieId);
            if (!sts.isEmpty()) stId = sts.get(0).getId();
        }
        if (stId != null) {
            List<Seat> seats = seatRepository.findByShowtimeId(stId);
            var requested = request.getSeats().stream().collect(Collectors.toSet());
            for (Seat s : seats) {
                if (requested.contains(s.getSeatCode())) {
                    if (s.isSold()) {
                        return ResponseEntity.badRequest().body(Map.of("message", "Seat already sold: " + s.getSeatCode()));
                    }
                    s.setSold(true);
                    seatRepository.save(s);
                }
            }
        }
        Booking booking = Booking.builder()
                .movieId(request.getMovieId())
                .showtime(request.getShowtime())
                .seats(request.getSeats())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .bookingCode(code)
                .build();
        bookingRepository.save(booking);

        Map<String, Object> resp = new HashMap<>();
        resp.put("status", "SUCCESS");
        resp.put("bookingCode", code);
        return ResponseEntity.ok(resp);
    }
}


