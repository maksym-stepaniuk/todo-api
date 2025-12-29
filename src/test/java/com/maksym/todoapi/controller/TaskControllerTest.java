package com.maksym.todoapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maksym.todoapi.dto.TaskCreateRequest;
import com.maksym.todoapi.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTask_shouldReturn201() throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("Learn testing");
        request.setDescription("Test");
        request.setPriority(1);

        mockMvc.perform(post("/tasks")
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
        request.setTitle("");
        request.setPriority(null);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));

    }

    @Test
    void getTask_notExisting_shouldReturn404() throws Exception {

        mockMvc.perform(get("/tasks/00000000-0000-0000-0000-000000000000"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("TASK_NOT_FOUND"));
    }

    @Test
    void updateTask_shouldReturn200() throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("old title");
        request.setDescription("old desc");
        request.setPriority(2);

        String response = mockMvc.perform(post("/tasks")
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
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("new title"))
                .andExpect(jsonPath("$.priority").value(1));
    }

    @Test
    void deleteTask_shouldReturn204() throws Exception {

        TaskCreateRequest request = new TaskCreateRequest();
        request.setTitle("old title");
        request.setDescription("old desc");
        request.setPriority(2);

        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(delete("/tasks/" + id))
                .andExpect(status().isNoContent());
    }
}
