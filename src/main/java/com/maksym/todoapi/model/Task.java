package com.maksym.todoapi.model;

import java.time.Instant;
import java.util.UUID;

public class Task {
    private UUID id;
    private String title;
    private String description;
    private TaskStatus status;
    private Integer priority;
    private Instant createdAt;
    private Instant dueAt;

    public Task(UUID id, String title, String description, TaskStatus status, Integer priority,Instant createdAt, Instant dueAt) {
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

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public void setDueAt(Instant dueAt) { this.dueAt = dueAt; }

}
