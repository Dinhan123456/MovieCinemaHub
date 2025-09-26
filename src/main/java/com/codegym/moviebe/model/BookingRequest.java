package com.codegym.moviebe.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {
    @NotNull(message = "movieId không được null")
    @Min(value = 1, message = "movieId phải > 0")
    private Long movieId;

    private Long showtimeId;

    @NotBlank(message = "showtime bắt buộc")
    private String showtime;

    @NotEmpty(message = "seats phải có ít nhất 1 ghế")
    private List<@Pattern(regexp = "[A-Z][0-9]+", message = "mã ghế không hợp lệ") String> seats;

    @NotBlank(message = "name bắt buộc")
    private String name;

    @Email(message = "email không hợp lệ")
    @NotBlank(message = "email bắt buộc")
    private String email;

    @NotBlank(message = "phone bắt buộc")
    @Pattern(regexp = "^[0-9]{10}$", message = "số điện thoại phải gồm đúng 10 chữ số")
    private String phone;
}


