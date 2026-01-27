package com.maksym.todoapi.controller;

import com.maksym.todoapi.BaseIntegrationTest;
import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.entity.TaskEntity;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.model.TaskStatus;
import com.maksym.todoapi.model.UserRole;
import com.maksym.todoapi.repository.ProjectRepository;
import com.maksym.todoapi.repository.TaskRepository;
import com.maksym.todoapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class ProjectTaskControllerTest extends BaseIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private MockMvc mockMvc;

    private UserEntity owner;
    private ProjectEntity project;

    @BeforeEach
    public void setUp() {
        owner = userRepository.save(new UserEntity(UUID.randomUUID(), "owner@test.com",passwordEncoder.encode("change-me"), Instant.now(), Set.of(UserRole.USER)));
        project = projectRepository.save(new ProjectEntity(UUID.randomUUID(), "Project Owner", owner));

        Instant base = Instant.parse("2026-01-10T00:00:00Z");

        saveTask("Deploy API", "deploy", TaskStatus.IN_PROGRESS, 1,
                base, Instant.parse("2026-01-20T00:00:00Z"));
        saveTask("Deploy UI", "deploy", TaskStatus.IN_PROGRESS, 1,
                base.plus(1, ChronoUnit.DAYS), Instant.parse("2026-01-21T10:00:00Z"));
        saveTask("Deploy Docs", "deploy", TaskStatus.IN_PROGRESS, 1,
                base.plus(2, ChronoUnit.DAYS), Instant.parse("2026-01-22T10:00:00Z"));
        saveTask("Fix bug", "deploy", TaskStatus.DONE, 1,
                base.plus(3, ChronoUnit.DAYS), Instant.parse("2026-01-23T10:00:00Z"));
        saveTask("Deploy API", "deploy", TaskStatus.IN_PROGRESS, 2,
                base.plus(4, ChronoUnit.DAYS), Instant.parse("2026-01-24T10:00:00Z"));
    }

    @Test
    void listTasks_withFiltersSearchSortAndPagination() throws Exception {
        mockMvc.perform(get("/projects/" + project.getId() + "/tasks")
                .with(jwt().jwt(jwt -> jwt.subject(owner.getId().toString())))
                .param("status", "IN_PROGRESS")
                .param("priority", "1")
                .param("dueFrom", "2026-01-19T10:00:00Z")
                .param("dueTo", "2026-01-23T10:00:00Z")
                .param("q", "deploy")
                .param("page", "0")
                .param("size", "2")
                .param("sort", "createdAt,desc")
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.content[0].title").value("Deploy Docs"))
                .andExpect(jsonPath("$.content[1].title").value("Deploy UI"));
    }

    @Test
    void listTasks_otherUserShouldNotSeeProject() throws Exception {
        UUID otherUser = UUID.randomUUID();
        mockMvc.perform(get("/projects/" + project.getId() + "/tasks")
                .with(jwt().jwt(jwt -> jwt.subject("11111111-1111-1111-1111-111111111112")))
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void listTasks_withoutHeader_shouldReturn401() throws Exception {
        mockMvc.perform(get("/projects/" + project.getId() + "/tasks"))
                .andExpect(status().isUnauthorized());
    }

    private void saveTask(String title, String description, TaskStatus status, Integer priority, Instant createdAt, Instant dueAt) {
        TaskEntity task =  new TaskEntity(
                UUID.randomUUID(),
                title,
                description,
                status,
                priority,
                createdAt,
                dueAt,
                project
        );
        taskRepository.save(task);
    }
}
