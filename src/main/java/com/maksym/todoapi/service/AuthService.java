package com.maksym.todoapi.service;

import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.exception.ConflictException;
import com.maksym.todoapi.model.UserRole;
import com.maksym.todoapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserEntity register(String email, String password) {

        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("Email already in use");
        }

        UserEntity user = new UserEntity(
                UUID.randomUUID(),
                email,
                passwordEncoder.encode(password),
                Instant.now(),
                Set.of(UserRole.USER)
        );

        return userRepository.save(user);
    }
}
