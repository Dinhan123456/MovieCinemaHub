package com.codegym.moviebe.controller;

import com.codegym.moviebe.entity.Role;
import com.codegym.moviebe.entity.User;
import com.codegym.moviebe.repository.RoleRepository;
import com.codegym.moviebe.repository.UserRepository;
import com.codegym.moviebe.security.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000", "http://127.0.0.1:3000"}, allowCredentials = "true")
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
    }

    public record RegisterRequest(
            @NotBlank(message = "username bắt buộc")
            @Pattern(regexp = "^\\S+$", message = "username không chứa khoảng trắng")
            String username,

            @NotBlank(message = "password bắt buộc")
            @Size(max = 16, message = "password tối đa 16 ký tự")
            String password,

            @NotBlank(message = "fullName bắt buộc")
            String fullName,

            @NotBlank(message = "email bắt buộc")
            @Pattern(regexp = "^[^@\n\r\t ]+@gmail\\.com$", message = "email phải có đuôi @gmail.com")
            String email
    ) {}
    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        if (userRepository.existsByUsername(req.username())) {
            return ResponseEntity.badRequest().body(Map.of("message", "username đã tồn tại"));
        }
        Role userRole = roleRepository.findByName("ROLE_USER").orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
        User u = User.builder()
                .username(req.username())
                .password(passwordEncoder.encode(req.password()))
                .fullName(req.fullName())
                .email(req.email())
                .roles(Set.of(userRole))
                .build();
        userRepository.save(u);
        String token = jwtService.generateToken(u.getUsername(), Map.of("roles", u.getRoles().stream().map(Role::getName).toList()));
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        var uOpt = userRepository.findByUsername(req.username());
        if (uOpt.isEmpty() || !passwordEncoder.matches(req.password(), uOpt.get().getPassword())) {
            return ResponseEntity.status(401).body(Map.of("message", "Sai tài khoản hoặc mật khẩu"));
        }
        var u = uOpt.get();
        String token = jwtService.generateToken(u.getUsername(), Map.of("roles", u.getRoles().stream().map(Role::getName).toList()));
        Map<String, Object> resp = new HashMap<>();
        resp.put("token", token);
        resp.put("username", u.getUsername());
        resp.put("roles", u.getRoles().stream().map(Role::getName).toList());
        return ResponseEntity.ok(resp);
    }
}


