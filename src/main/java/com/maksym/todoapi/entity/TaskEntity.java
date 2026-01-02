package com.maksym.todoapi.entity;

import com.maksym.todoapi.model.TaskStatus;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private Instant createdAt;

    private Instant dueAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "project_id")
    private ProjectEntity project;

    protected TaskEntity() {
    }

    public TaskEntity(
            UUID id,
            String title,
            String description,
            TaskStatus status,
            Integer priority,
            Instant createdAt,
            Instant dueAt,
            ProjectEntity project
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.dueAt = dueAt;
        this.project = project;
    }

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public Integer getPriority() { return priority; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getDueAt() { return dueAt; }
    public ProjectEntity getProject() { return project; }
}
