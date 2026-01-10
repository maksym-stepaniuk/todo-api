package com.maksym.todoapi.repository;

import com.maksym.todoapi.entity.TaskEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<TaskEntity, UUID>, JpaSpecificationExecutor<TaskEntity> {

    @Override
    @EntityGraph(attributePaths = {"project"})
    Page<TaskEntity> findAll(Specification<TaskEntity> spec, Pageable pageable);
}
