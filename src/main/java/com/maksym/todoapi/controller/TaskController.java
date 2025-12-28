package com.maksym.todoapi.controller;


import com.maksym.todoapi.dto.TaskStatusUpdateRequest;
import com.maksym.todoapi.dto.TaskUpdateRequest;
import com.maksym.todoapi.model.Task;
import com.maksym.todoapi.service.TaskService;
import com.maksym.todoapi.dto.TaskCreateRequest;
import com.maksym.todoapi.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.boot.autoconfigure.http.client.AbstractHttpClientProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final AbstractHttpClientProperties abstractHttpClientProperties;

    public TaskController(TaskService taskService, AbstractHttpClientProperties abstractHttpClientProperties) {
        this.taskService = taskService;
        this.abstractHttpClientProperties = abstractHttpClientProperties;
    }

    private TaskResponse toResponse(Task task) {
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

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String sort
    ) {
        List<TaskResponse> tasks = taskService.getAll().stream()
                .filter(task -> {
                    if (status == null) { return true; }
                    return task.getStatus().name().equalsIgnoreCase(status);
                })

                .filter(task -> {
                    if (query == null) {
                        return true;
                    }

                    String q = query.toLowerCase();

                    return ((task.getTitle() != null && task.getTitle().toLowerCase().contains(q))
                            || (task.getDescription() != null && task.getDescription().toLowerCase().contains(q)));
                })

                .sorted((a, b) -> {
                        if (sort == null) { return 0; }

                        return switch (sort) {
                            case "priority" -> a.getPriority().compareTo(b.getPriority());
                            case "dueAt" -> {
                                if (a.getDueAt() == null && b.getDueAt() == null) yield 0;
                                if (a.getDueAt() == null) yield 1;
                                if (b.getDueAt() == null) yield -1;

                                yield a.getDueAt().compareTo(b.getDueAt());
                            }
                            default -> 0;
                        };
                })

                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getById(@PathVariable UUID id) {
        Task task = taskService.getById(id);

        return ResponseEntity.ok(toResponse(task));
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(
            @Valid @RequestBody TaskCreateRequest request
    ) {
        Task task = taskService.create(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                request.getDueAt()
        );

        return ResponseEntity.status(201).body(toResponse(task));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody TaskUpdateRequest request
    ) {
        Task task = taskService.update(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                request.getDueAt()
        );

        return ResponseEntity.ok(toResponse(task));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody TaskStatusUpdateRequest request
    ) {
        Task task = taskService.updateStatus(id, request.getStatus());

        return ResponseEntity.ok(toResponse(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
