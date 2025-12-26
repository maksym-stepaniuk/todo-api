package com.maksym.todoapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class TaskUpdateRequest {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    private Integer priority;

    private Instant dueAt;
}