package com.maksym.todoapi.repository;

import com.maksym.todoapi.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<ProjectEntity, UUID> {

    Optional<ProjectEntity> findByIdAndUser_Id(UUID id, UUID userId);
    Optional<ProjectEntity> findFirstByUser_Id(UUID userId);
}
