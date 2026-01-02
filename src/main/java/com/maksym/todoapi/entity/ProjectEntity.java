package com.maksym.todoapi.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "projects")
public class ProjectEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TaskEntity> tasks = new ArrayList<>();

    protected ProjectEntity(){
    }

    public ProjectEntity(UUID id, String name, UserEntity user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public UserEntity getUser() { return user; }
    public List<TaskEntity> getTasks() { return tasks; }
}
