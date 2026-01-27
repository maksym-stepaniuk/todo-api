package com.maksym.todoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.Instant;
import java.util.UUID;

public class TaskCreateRequest {

    @NotNull
    private UUID projectId;

    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    private Integer priority;

    private Instant dueAt;

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getPriority() { return priority; }
    public Instant getDueAt() { return dueAt; }
    public UUID getProjectId() { return projectId; }

    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public void setDueAt(Instant dueAt) { this.dueAt = dueAt; }
    public void setProjectId(UUID projectId) { this.projectId = projectId; }
}
