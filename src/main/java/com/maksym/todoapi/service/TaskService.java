package com.maksym.todoapi.service;

import com.maksym.todoapi.model.Task;
import com.maksym.todoapi.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
public class TaskService {
    private final Map<UUID, Task> storage = new HashMap<>();

    public TaskService() {
        Task task1 = new Task(
                UUID.randomUUID(),
                "Learn spring boot",
                "Make first endpoints",
                TaskStatus.TODO,
                1,
                Instant.now(),
                null
        );

        Task task2 = new Task(
                UUID.randomUUID(),
                "Write tests",
                "MockMvc basics",
                TaskStatus.IN_PROGRESS,
                2,
                Instant.now(),
                null
        );

        storage.put(task1.getId(), task1);
        storage.put(task2.getId(), task2);
    }

    public List<Task> getAll() {
        return new ArrayList<>(storage.values());
    }

    public Optional<Task> getById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public Task create(String title, String description, Integer priority, Instant dueAt) {
        Task task = new Task(
                UUID.randomUUID(),
                title,
                description,
                TaskStatus.TODO,
                priority,
                Instant.now(),
                dueAt
        );

        storage.put(task.getId(), task);
        return task;
    }
}
