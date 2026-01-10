package com.maksym.todoapi.service;

import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.entity.TaskEntity;
import com.maksym.todoapi.exception.TaskNotFoundException;
import com.maksym.todoapi.model.TaskStatus;
import com.maksym.todoapi.repository.ProjectRepository;
import com.maksym.todoapi.repository.TaskRepository;
import jakarta.transaction.Transactional;
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

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    private ProjectEntity getDefaultProject() {
        return projectRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Default project not found"));
    }

    public List<TaskEntity> getAll() {
        return taskRepository.findAll();
    }

    public TaskEntity getById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
    }

    @Transactional
    public TaskEntity create(String title, String description, Integer priority, Instant dueAt) {
        TaskEntity task = new TaskEntity(
                UUID.randomUUID(),
                title,
                description,
                TaskStatus.TODO,
                priority,
                Instant.now(),
                dueAt,
                getDefaultProject()
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

    public Page<TaskEntity> getTasksByProject(
            UUID projectId,
            List<TaskStatus> statuses,
            List<Integer> priorities,
            Instant dueFrom,
            Instant dueTo,
            Pageable pageable
    ) {
        Specification<TaskEntity> spec = Specification.where(belongsToProject(projectId))
                .and(hasStatuses(statuses))
                .and(hasPriorities(priorities))
                .and(dueFrom(dueFrom))
                .and(dueTo(dueTo));

        return taskRepository.findAll(spec, pageable);
    }
}
