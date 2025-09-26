package com.codegym.moviebe.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "seats")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long showtimeId;

    @Column(nullable = false)
    private String seatCode; // e.g. A1, B7

    @Column(nullable = false)
    private boolean sold;
}


