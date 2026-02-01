package com.maksym.todoapi.service;

import com.maksym.todoapi.dto.LoginResponse;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.exception.ConflictException;
import com.maksym.todoapi.exception.UnauthorizedException;
import com.maksym.todoapi.model.UserRole;
import com.maksym.todoapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtEncoder jwtEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_shouldSaveUserWithEncodedPassword() {
        String email = "test@test.com";
        String rawPassword = "Password123";
        String hashed = "hashed";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(hashed);
        when(userRepository.save(any(UserEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        UserEntity saved = authService.register(email, rawPassword);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getEmail()).isEqualTo(email);
        assertThat(saved.getHashPassword()).isEqualTo(hashed);
        assertThat(saved.getCreateAt()).isNotNull();
        assertThat(saved.getRoles()).contains(UserRole.USER);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).encode(rawPassword);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void register_shouldThrowConflict_whenEmailAlreadyUsed() {
        String email = "test@test.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new UserEntity(UUID.randomUUID(), email, "x", Instant.now(), Set.of(UserRole.USER))));

        assertThatThrownBy(() -> authService.register(email, "any")).isInstanceOf(ConflictException.class);

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_shouldReturnToken_whenCredentialsValid() {
        String email = "test@test.com";
        String password = "Password123";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));

        UserEntity user = new UserEntity(UUID.randomUUID(), email, "hashed", Instant.now(), Set.of(UserRole.USER));
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        Jwt jwt = new Jwt(
                "token",
                Instant.now(),
                Instant.now().plusSeconds(1800),
                Map.of("alg", "HS256"),
                Map.of("sub", user.getId().toString())
        );
        when(jwtEncoder.encode(any())).thenReturn(jwt);

        LoginResponse response = authService.login(email, password);

        assertThat(response.getAccessToken()).isEqualTo("token");
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        assertThat(response.getExpiresIn()).isEqualTo(1800L);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByEmail(email);
        verify(jwtEncoder).encode(any());
    }

    @Test
    void login_shouldThrowUnauthorized_whenAuthenticationFails() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad"));

        assertThatThrownBy(() -> authService.login("test@test.com", "Password123"))
                .isInstanceOf(UnauthorizedException.class);

        verify(userRepository, never()).findByEmail(any());
        verify(jwtEncoder, never()).encode(any());
    }

    @Test
    void login_shouldThrowUnauthorized_whenUserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));

        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login("missing@test.com", "Password123"))
                .isInstanceOf(UnauthorizedException.class);

        verify(jwtEncoder, never()).encode(any());
    }
}
