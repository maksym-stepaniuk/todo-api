package com.maksym.todoapi.service;

import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.exception.ConflictException;
import com.maksym.todoapi.exception.UnauthorizedException;
import com.maksym.todoapi.repository.ProjectRepository;
import com.maksym.todoapi.repository.UserRepository;
import com.maksym.todoapi.security.UserContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectService(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    private UUID currentUserId() {

        UUID userId = UserContext.get();

        if(userId == null) {
            throw new UnauthorizedException("X-User-Id header is required");
        }

        return userId;
    }

    @Transactional
    public ProjectEntity create(String name) {

        UUID userId = currentUserId();

        if(projectRepository.existsByUser_IdAndName(userId, name)) {
            throw new ConflictException("Project name already exists");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        ProjectEntity project = new ProjectEntity(UUID.randomUUID(), name, user);

        return projectRepository.save(project);
    }
}
