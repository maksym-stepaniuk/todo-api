package com.maksym.todoapi.service;

import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.exception.ConflictException;
import com.maksym.todoapi.exception.UnauthorizedException;
import com.maksym.todoapi.repository.ProjectRepository;
import com.maksym.todoapi.repository.UserRepository;
import com.maksym.todoapi.security.CurrentUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ProjectService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUser currentUser;

    public ProjectService(UserRepository userRepository, ProjectRepository projectRepository, CurrentUser currentUser) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.currentUser = currentUser;
    }

    private UUID currentUserId() {
        return currentUser.id();
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
