package com.maksym.todoapi.controller;


import com.maksym.todoapi.dto.TaskStatusUpdateRequest;
import com.maksym.todoapi.dto.TaskUpdateRequest;
import com.maksym.todoapi.model.Task;
import com.maksym.todoapi.service.TaskService;
import com.maksym.todoapi.dto.TaskCreateRequest;
import com.maksym.todoapi.dto.TaskResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(@PathVariable UUID id) {
        return taskService.getById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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

        TaskResponse response = new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getCreatedAt(),
                task.getDueAt()
        );

        return ResponseEntity.status(201).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody TaskUpdateRequest request
    ) {
        return taskService.update(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                request.getDueAt()
        ).map(task -> ResponseEntity.ok(
                new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getCreatedAt(),
                        task.getDueAt()
                )
        )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody TaskStatusUpdateRequest request
    ) {
        return taskService.updateStatus(id, request.getStatus())
                .map(task -> ResponseEntity.ok(
                        new TaskResponse(
                                task.getId(),
                                task.getTitle(),
                                task.getDescription(),
                                task.getStatus(),
                                task.getPriority(),
                                task.getCreatedAt(),
                                task.getDueAt()
                        )
                )).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean removed = taskService.delete(id);

        if(removed) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
