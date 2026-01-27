package com.maksym.todoapi.controller;

import com.maksym.todoapi.dto.ProjectCreateRequest;
import com.maksym.todoapi.dto.ProjectResponse;
import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody ProjectCreateRequest request) {
        ProjectEntity project = projectService.create(request.getName());
        return ResponseEntity.status(201).body(new ProjectResponse(project.getId(), project.getName()));
    }
}
