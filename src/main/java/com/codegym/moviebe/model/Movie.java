package com.codegym.moviebe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    private Long id;
    private String title;
    private String originalTitle;
    private Integer year;
    private List<String> genre;
    private Double rating;
    private String duration; // e.g. "152 phút"
    private String image;
    private String description;
    private Integer price; // VNĐ
    private List<String> showtimes; // e.g. ["14:00", "16:30"]
}


