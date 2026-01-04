package com.maksym.todoapi.controller;

import com.maksym.todoapi.dto.PageResponse;
import com.maksym.todoapi.dto.TaskResponse;
import com.maksym.todoapi.entity.TaskEntity;
import com.maksym.todoapi.model.TaskStatus;
import com.maksym.todoapi.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/projects/{projectId}/tasks")
public class ProjectTaskController {

    private final TaskService taskService;

    public ProjectTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public PageResponse<TaskResponse> getTasks(
            @PathVariable UUID projectId,
            @RequestParam(required = false) TaskStatus status,
            Pageable pageable
    ) {
        Page<TaskEntity> page = taskService.getTasksByProject(projectId, status, pageable);

        return new PageResponse<>(
                page.getContent().stream()
                    .map(this::toResponse)
                    .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    private TaskResponse toResponse(TaskEntity task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getCreatedAt(),
                task.getDueAt()
        );
    }
}
