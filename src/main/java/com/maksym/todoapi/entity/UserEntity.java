package com.maksym.todoapi.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    private List<ProjectEntity> projects = new ArrayList<>();

    protected UserEntity(){
    }

    public UserEntity(UUID id, String email) {
        this.id = id;
        this.email = email;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public List<ProjectEntity> getProjects() { return projects; }
}
