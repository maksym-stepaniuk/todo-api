package com.maksym.todoapi.dto;

import jakarta.validation.constraints.NotNull;

public class RoleUpdateRequest {
    @NotNull
    private Boolean admin;

    public void setAdmin(Boolean admin) { this.admin = admin; }
    public Boolean getAdmin() { return admin; }
}
