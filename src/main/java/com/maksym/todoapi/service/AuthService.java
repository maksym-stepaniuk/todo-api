package com.maksym.todoapi.service;

import com.maksym.todoapi.dto.LoginResponse;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.exception.ConflictException;
import com.maksym.todoapi.exception.UnauthorizedException;
import com.maksym.todoapi.model.UserRole;
import com.maksym.todoapi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtEncoder jwtEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtEncoder = jwtEncoder;
        this.authenticationManager = authenticationManager;
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

    @Transactional(readOnly = true)
    public LoginResponse login(String email, String password) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("Invalid credentials");
        }

        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

        Instant now = Instant.now();
        Instant exp = now.plus(Duration.ofMinutes(30));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .issuedAt(now)
                .expiresAt(exp)
                .claim("roles", user.getRoles().stream().map(Enum::name).toList())
                .claim("email", user.getEmail())
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        String token = jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();

        return new LoginResponse(token, "Bearer", Duration.between(now, exp).getSeconds());
    }
}
