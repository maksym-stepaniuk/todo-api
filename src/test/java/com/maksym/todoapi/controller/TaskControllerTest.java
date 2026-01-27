package com.maksym.todoapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.todoapi.BaseIntegrationTest;
import com.maksym.todoapi.dto.TaskCreateRequest;
import com.maksym.todoapi.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskControllerTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String DEFAULT_USER_ID = "11111111-1111-1111-1111-111111111111";
    private static final UUID DEFAULT_PROJECT_ID = UUID.fromString("22222222-2222-2222-2222-222222222222");

    @Test
    void createTask_shouldReturn201() throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setProjectId(DEFAULT_PROJECT_ID);
        request.setTitle("Learn testing");
        request.setDescription("Test");
        request.setPriority(1);

        mockMvc.perform(post("/tasks")
                        .with(jwt())
                        .header("X-User-Id", DEFAULT_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Learn testing"))
                .andExpect(jsonPath("$.status").value(TaskStatus.TODO.name()));
    }

    @Test
    void createTask_invalidRequest_shouldReturn400() throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setProjectId(DEFAULT_PROJECT_ID);
        request.setTitle("");
        request.setPriority(null);

        mockMvc.perform(post("/tasks")
                        .with(jwt())
                        .header("X-User-Id", DEFAULT_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));

    }

    @Test
    void getTask_notExisting_shouldReturn404() throws Exception {

        mockMvc.perform(get("/tasks/00000000-0000-0000-0000-000000000000")
                        .with(jwt())
                        .header("X-User-Id", DEFAULT_USER_ID))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    void updateTask_shouldReturn200() throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setProjectId(DEFAULT_PROJECT_ID);
        request.setTitle("old title");
        request.setDescription("old desc");
        request.setPriority(2);

        String response = mockMvc.perform(post("/tasks")
                    .with(jwt())
                    .header("X-User-Id", DEFAULT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        String updatedJson = """
                {
                  "title": "new title",
                  "description": "new desc",
                  "priority": 1
                }
                """;

        mockMvc.perform(put("/tasks/" + id)
                    .with(jwt())
                    .header("X-User-Id", DEFAULT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new title"))
                .andExpect(jsonPath("$.priority").value(1));
    }

    @Test
    void deleteTask_shouldReturn204() throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setProjectId(DEFAULT_PROJECT_ID);
        request.setTitle("old title");
        request.setDescription("old desc");
        request.setPriority(2);

        String response = mockMvc.perform(post("/tasks")
                        .with(jwt())
                        .header("X-User-Id", DEFAULT_USER_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(delete("/tasks/" + id)
                        .with(jwt())
                        .header("X-User-Id", DEFAULT_USER_ID))
                .andExpect(status().isNoContent());
    }
}
