package com.maksym.todoapi.config;

import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.repository.ProjectRepository;
import com.maksym.todoapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public DataInitializer(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    private static final UUID DEFAULT_USER_ID = UUID.fromString("11111111-1111-1111-1111-111111111111");
    private static final UUID DEFAULT_PROJECT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @PostConstruct
    public void init() {
        UserEntity user = userRepository.findByEmail("test@test.com")
                        .orElseGet(() -> userRepository.save(new UserEntity(DEFAULT_USER_ID, "test@test.com")));

        projectRepository.findFirstByUser_Id(user.getId())
                .orElseGet(() -> projectRepository.save(new ProjectEntity(DEFAULT_PROJECT_ID, "Default Project", user)));
    }
}
