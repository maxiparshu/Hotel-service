package by.beltam.practice.controller;

import by.beltam.practice.dto.AuthResponse;
import by.beltam.practice.dto.UserRequest;
import by.beltam.practice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID register(@Valid @RequestBody UserRequest request) {
        return authService.register(request).getId();
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody UserRequest request) {
        return authService.login(request);
    }

    @PatchMapping("/promote/{id}")
    public void promoteToAdmin(@PathVariable UUID id) {
        authService.promoteToAdmin(id);
    }
}