package com.maksym.todoapi.service;

import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.entity.TaskEntity;
import com.maksym.todoapi.exception.ProjectNotFoundException;
import com.maksym.todoapi.exception.TaskNotFoundException;
import com.maksym.todoapi.model.TaskStatus;
import com.maksym.todoapi.repository.ProjectRepository;
import com.maksym.todoapi.repository.TaskRepository;
import com.maksym.todoapi.security.CurrentUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

import static com.maksym.todoapi.repository.TaskSpecifications.*;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final CurrentUser currentUser;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, CurrentUser currentUser) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.currentUser = currentUser;
    }

    private UUID currentUserId() {
        return currentUser.id();
    }

    @Transactional(readOnly = true)
    public List<TaskEntity> getAll() {
        return taskRepository.findAllByProject_User_Id(currentUserId());
    }

    @Transactional(readOnly = true)
    public TaskEntity getById(UUID id) {
        return taskRepository.findByIdAndProject_User_Id(id, currentUserId())
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    public TaskEntity create(UUID projectId, String title, String description, Integer priority, Instant dueAt) {
        UUID userId = currentUserId();

        ProjectEntity project = projectRepository.findByIdAndUser_Id(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

        TaskEntity task = new TaskEntity(
                UUID.randomUUID(),
                title,
                description,
                TaskStatus.TODO,
                priority,
                Instant.now(),
                dueAt,
                project
        );

        return taskRepository.save(task);
    }

    @Transactional
    public TaskEntity update(UUID id, String title, String description, Integer priority, Instant dueAt) {
        TaskEntity task = getById(id);
        task.updateDetails(title, description, priority, dueAt);
        return task;
    }

    @Transactional
    public TaskEntity updateStatus(UUID id, TaskStatus status) {
        TaskEntity task = getById(id);
        task.updateStatus(status);
        return task;
    }

    @Transactional
    public void delete(UUID id) {
        TaskEntity task = getById(id);
        taskRepository.delete(task);
    }

    @Transactional(readOnly = true)
    public Page<TaskEntity> getTasksByProject(
            UUID projectId,
            List<TaskStatus> statuses,
            List<Integer> priorities,
            Instant dueFrom,
            Instant dueTo,
            String q,
            Pageable pageable
    ) {
        UUID userId = currentUserId();

        projectRepository.findByIdAndUser_Id(projectId, userId)
                .orElseThrow(() -> new ProjectNotFoundException("Project not found"));


        Specification<TaskEntity> spec = Specification.where(belongsToProject(projectId))
                .and(belongsToUser(userId))
                .and(hasStatuses(statuses))
                .and(hasPriorities(priorities))
                .and(dueFrom(dueFrom))
                .and(dueTo(dueTo))
                .and(matchesQuery(q));

        return taskRepository.findAll(spec, pageable);
    }
}
