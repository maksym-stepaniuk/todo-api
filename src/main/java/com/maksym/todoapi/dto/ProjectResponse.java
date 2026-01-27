package com.maksym.todoapi.dto;


import java.util.UUID;

public class ProjectResponse {

    private UUID id;
    private String name;

    public ProjectResponse(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() { return name; }
    public UUID getId() { return id; }
}
