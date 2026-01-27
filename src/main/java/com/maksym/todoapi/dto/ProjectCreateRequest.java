package com.maksym.todoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProjectCreateRequest {

    @NotBlank
    @Size(max = 255)
    private String name;

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
}
