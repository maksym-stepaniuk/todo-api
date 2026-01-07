package com.maksym.todoapi.repository;

import com.maksym.todoapi.entity.TaskEntity;
import com.maksym.todoapi.model.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID> {

    @EntityGraph(attributePaths = {"project"})
    Page<TaskEntity> findAllByProject_Id(UUID projectId, Pageable pageable);

    @EntityGraph(attributePaths = {"project"})
    Page<TaskEntity> findAllByProject_IdAndStatus(UUID projectId, TaskStatus status, Pageable pageable);
}
