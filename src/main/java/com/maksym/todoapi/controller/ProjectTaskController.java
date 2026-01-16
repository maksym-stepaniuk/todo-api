package com.maksym.todoapi.controller;

import com.maksym.todoapi.dto.PageResponse;
import com.maksym.todoapi.dto.TaskResponse;
import com.maksym.todoapi.entity.TaskEntity;
import com.maksym.todoapi.exception.BadRequestException;
import com.maksym.todoapi.model.TaskStatus;
import com.maksym.todoapi.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/projects/{projectId}/tasks")
public class ProjectTaskController {

    private final TaskService taskService;
    private static final int MAX_PAGE_SIZE = 100;

    public ProjectTaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    private void validatePageable(Pageable pageable) {
        if(pageable.getPageSize() > MAX_PAGE_SIZE) {
            throw new BadRequestException("size must be <= " + MAX_PAGE_SIZE);
        }
    }

    @GetMapping
    public PageResponse<TaskResponse> getTasks(
            @PathVariable UUID projectId,
            @RequestParam(required = false) List<TaskStatus> status,
            @RequestParam(required = false) List<Integer> priority,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dueFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant dueTo,
            @RequestParam(required = false) String q,
            @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        validatePageable(pageable);
        Page<TaskEntity> page = taskService.getTasksByProject(projectId, status, priority, dueFrom, dueTo, q, pageable);

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
