package com.maksym.todoapi;

import com.maksym.todoapi.entity.ProjectEntity;
import com.maksym.todoapi.entity.TaskEntity;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.model.TaskStatus;
import com.maksym.todoapi.repository.ProjectRepository;
import com.maksym.todoapi.repository.TaskRepository;
import com.maksym.todoapi.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TaskIntegrationTest extends BaseIntegrationTest{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void fullFlow_userProjectTaskWorks() {

        UserEntity user = new UserEntity(
                UUID.randomUUID(),
                "integration@test.com"
        );
        userRepository.save(user);

        ProjectEntity project = new ProjectEntity(
                UUID.randomUUID(),
                "Integration Project",
                user
        );
        projectRepository.save(project);

        TaskEntity task = new TaskEntity(
                UUID.randomUUID(),
                "Integration task",
                "Check DB",
                TaskStatus.TODO,
                1,
                Instant.now(),
                null,
                project
        );
        taskRepository.save(task);

        TaskEntity fetched = taskRepository.findById(task.getId()).orElseThrow();

        assertThat(fetched.getTitle()).isEqualTo("Integration task");
        assertThat(fetched.getProject().getName()).isEqualTo("Integration Project");
        assertThat(fetched.getStatus()).isEqualTo(TaskStatus.TODO);
    }
}
