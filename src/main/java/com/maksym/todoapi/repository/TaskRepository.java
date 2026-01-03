package com.maksym.todoapi.repository;

import com.maksym.todoapi.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {
}
