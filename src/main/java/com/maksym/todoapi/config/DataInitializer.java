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

    @PostConstruct
    public void init() {

        if (userRepository.count() > 0) {
            return;
        }

        UserEntity user = new UserEntity(UUID.randomUUID(), "test@test.com");
        userRepository.save(user);

        ProjectEntity project = new ProjectEntity(UUID.randomUUID(), "Default project", user);
        projectRepository.save(project);
    }
}
