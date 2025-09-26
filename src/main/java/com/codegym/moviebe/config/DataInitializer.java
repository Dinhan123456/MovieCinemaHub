package com.codegym.moviebe.config;

import com.codegym.moviebe.entity.Movie;
import com.codegym.moviebe.entity.Seat;
import com.codegym.moviebe.entity.Showtime;
import com.codegym.moviebe.entity.Role;
import com.codegym.moviebe.repository.MovieRepository;
import com.codegym.moviebe.repository.SeatRepository;
import com.codegym.moviebe.repository.ShowtimeRepository;
import com.codegym.moviebe.repository.RoleRepository;
import com.codegym.moviebe.repository.UserRepository;
import com.codegym.moviebe.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedMovies(MovieRepository movieRepository, RoleRepository roleRepository, ShowtimeRepository showtimeRepository, SeatRepository seatRepository, UserRepository userRepository) {
        return args -> {
            if (roleRepository.findByName("ROLE_USER").isEmpty()) {
                roleRepository.save(Role.builder().name("ROLE_USER").build());
            }
            if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
                roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
            }

            // seed admin user if absent
            if (userRepository.findByUsername("admin").isEmpty()) {
                var adminRole = roleRepository.findByName("ROLE_ADMIN").get();
                var enc = new BCryptPasswordEncoder();
                userRepository.save(User.builder()
                        .username("admin")
                        .password(enc.encode("admin123"))
                        .fullName("Administrator")
                        .email("admin@gmail.com")
                        .roles(java.util.Set.of(adminRole))
                        .build());
            }

            if (movieRepository.count() > 0) return;

            var m1 = movieRepository.save(Movie.builder()
                .title("Kỵ Sĩ Bóng Đêm")
                .originalTitle("The Dark Knight")
                .year(2008)
                .genre(Arrays.asList("Hành Động", "Tội Phạm", "Chính Kịch"))
                .rating(9.0)
                .duration("152 phút")
                .image("https://images.unsplash.com/photo-1489599804341-4b0b0b0b0b0b?w=300&h=450&fit=crop")
                .description("Khi kẻ thù Joker gây hỗn loạn, Batman đối mặt thử thách lớn nhất.")
                .price(120000)
                .showtimes(Arrays.asList("14:00", "16:30", "19:00", "21:30"))
                .build());

            var m2 = movieRepository.save(Movie.builder()
                .title("Khởi Đầu")
                .originalTitle("Inception")
                .year(2010)
                .genre(Arrays.asList("Hành Động", "Khoa Học Viễn Tưởng", "Kinh Dị"))
                .rating(8.8)
                .duration("148 phút")
                .image("https://images.unsplash.com/photo-1489599804341-4b0b0b0b0b0b?w=300&h=450&fit=crop")
                .description("Tên trộm sử dụng công nghệ chia sẻ giấc mơ để gieo ý tưởng.")
                .price(110000)
                .showtimes(Arrays.asList("13:30", "16:00", "18:30", "21:00"))
                .build());
            // seed showtimes for m1, m2 if empty
            if (showtimeRepository.count() == 0) {
                var st1 = showtimeRepository.save(Showtime.builder().movieId(m1.getId()).startTime(java.time.LocalDateTime.now().plusHours(2)).price(120000).auditorium("Room A").build());
                var st2 = showtimeRepository.save(Showtime.builder().movieId(m1.getId()).startTime(java.time.LocalDateTime.now().plusHours(5)).price(120000).auditorium("Room A").build());
                var st3 = showtimeRepository.save(Showtime.builder().movieId(m2.getId()).startTime(java.time.LocalDateTime.now().plusHours(3)).price(110000).auditorium("Room B").build());

                // simple seat map A1..A8, B1..B8
                for (String row : new String[]{"A","B","C","D"}) {
                    for (int i = 1; i <= 8; i++) {
                        String code = row + i;
                        seatRepository.save(Seat.builder().showtimeId(st1.getId()).seatCode(code).sold(false).build());
                        seatRepository.save(Seat.builder().showtimeId(st2.getId()).seatCode(code).sold(false).build());
                        seatRepository.save(Seat.builder().showtimeId(st3.getId()).seatCode(code).sold(false).build());
                    }
                }
            }
        };
    }
}


