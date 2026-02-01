package com.maksym.todoapi.service;

import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.exception.UserNotFoundException;
import com.maksym.todoapi.model.UserRole;
import com.maksym.todoapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserService adminUserService;

    @Test
    void updateUserRole_shouldAddAdmin_whenAdminTrue() {
        UUID id = UUID.randomUUID();
        UserEntity user = new UserEntity(id, "test@test.com", "hash", Instant.now(), new HashSet<>(Set.of(UserRole.USER)));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        adminUserService.updateUserRole(id, true);

        assertThat(user.getRoles()).contains(UserRole.ADMIN);
    }

    @Test
    void updateUserRole_shouldRemoveAdmin_whenAdminFalse() {
        UUID id = UUID.randomUUID();
        UserEntity user = new UserEntity(id, "test@test.com", "hash", Instant.now(), new HashSet<>(Set.of(UserRole.USER, UserRole.ADMIN)));
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        adminUserService.updateUserRole(id, false);

        assertThat(user.getRoles()).doesNotContain(UserRole.ADMIN);
    }

    @Test
    void updateUserRole_shouldThrow_whenUserMissing() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminUserService.updateUserRole(id, false))
                .isInstanceOf(UserNotFoundException.class);
    }
}
