package com.maksym.todoapi.config;

import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.model.UserRole;
import com.maksym.todoapi.repository.ProjectRepository;
import com.maksym.todoapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Component
public class DataInitializer {

    private static final UUID DEFAULT_USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DEFAULT_PROJECT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, ProjectRepository projectRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        UserEntity user = userRepository.findByEmail("test@test.com")
                        .orElseGet(() -> userRepository.save(new UserEntity(
                                DEFAULT_USER_ID,
                                "test@test.com",
                                passwordEncoder.encode("change-me"),
                                Instant.now(),
                                Set.of(UserRole.USER))
                                )
                        );

        projectRepository.findFirstByUser_Id(user.getId())
                .orElseGet(() -> projectRepository.save(new ProjectEntity(
                        DEFAULT_PROJECT_ID,
                        "Default Project",
                        user)));
    }
}
