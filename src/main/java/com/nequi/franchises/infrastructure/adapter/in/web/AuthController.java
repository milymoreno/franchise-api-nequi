package com.nequi.franchises.infrastructure.adapter.in.web;

import com.nequi.franchises.application.dto.LoginRequest;
import com.nequi.franchises.infrastructure.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Authentication endpoints")
public class AuthController {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin123";

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public Mono<ResponseEntity<Map<String, String>>> login(@Valid @RequestBody LoginRequest request) {
        if (ADMIN_USERNAME.equals(request.getUsername())
                && ADMIN_PASSWORD.equals(request.getPassword())) {
            String token = jwtService.generateToken(request.getUsername());
            return Mono.just(ResponseEntity.ok(Map.of("token", token)));
        }
        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Invalid credentials")));
    }
}
