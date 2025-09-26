package com.codegym.moviebe.service.impl;

import com.codegym.moviebe.model.Movie;
import com.codegym.moviebe.service.MovieService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class InMemoryMovieService implements MovieService {

    private List<Movie> movies;

    @PostConstruct
    public void init() {
        movies = List.of(
            Movie.builder()
                .id(1L)
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
                .build(),
            Movie.builder()
                .id(2L)
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
                .build()
        );
    }

    @Override
    public List<Movie> getAll() {
        return movies;
    }

    @Override
    public Optional<Movie> getById(Long id) {
        return movies.stream().filter(m -> m.getId().equals(id)).findFirst();
    }
}


