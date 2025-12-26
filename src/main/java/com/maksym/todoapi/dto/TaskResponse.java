package com.maksym.todoapi.dto;

import com.maksym.todoapi.model.TaskStatus;

import java.time.Instant;
import java.util.UUID;

public class TaskResponse {

    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private Integer priority;
    private Instant createdAt;
    private Instant dueAt;

    public TaskResponse(
            UUID id,
            String title,
            String description,
            TaskStatus status,
            Integer priority,
            Instant createdAt,
            Instant dueAt
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public Integer getPriority() { return priority; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getDueAt() { return dueAt; }
}
