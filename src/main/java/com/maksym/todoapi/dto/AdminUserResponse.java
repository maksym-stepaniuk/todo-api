package com.maksym.todoapi.dto;

import com.maksym.todoapi.model.UserRole;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public class AdminUserResponse {
    private UUID id;
    private String email;
    private Set<UserRole> roles;
    private Instant createdAt;

    public AdminUserResponse(UUID id, String email, Set<UserRole> roles, Instant createdAt) {
        this.id = id;
        this.email = email;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() { return createdAt; }
    public Set<UserRole> getRoles() { return roles; }
    public String getEmail() { return email; }
    public UUID getId() { return id; }
}
