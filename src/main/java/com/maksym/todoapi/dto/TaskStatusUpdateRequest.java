package com.maksym.todoapi.dto;

import com.maksym.todoapi.model.TaskStatus;
import jakarta.validation.constraints.NotNull;

public class TaskStatusUpdateRequest {
    @NotNull
    private TaskStatus status;

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
