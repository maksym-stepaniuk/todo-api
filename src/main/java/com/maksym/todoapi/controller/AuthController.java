package com.maksym.todoapi.controller;

import com.maksym.todoapi.dto.RegisterRequest;
import com.maksym.todoapi.dto.RegisterResponse;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        UserEntity user =  authService.register(request.getEmail(), request.getPassword());
        return ResponseEntity.status(201).body(new RegisterResponse(user.getId(), user.getEmail()));
    }
}
