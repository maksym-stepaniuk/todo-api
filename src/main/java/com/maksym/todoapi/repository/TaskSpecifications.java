package com.maksym.todoapi.repository;


import com.maksym.todoapi.entity.TaskEntity;
import com.maksym.todoapi.model.TaskStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public final class TaskSpecifications {
    private TaskSpecifications() {}

    public static Specification<TaskEntity> belongsToUser(UUID userId) {
        return (root, query, cb) -> cb.equal(root.get("project").get("user").get("id"), userId);
    }

    public static Specification<TaskEntity> belongsToProject(UUID projectId) {
        return (root, query, cb) -> cb.equal(root.get("project").get("id"), projectId);
    }

    public static Specification<TaskEntity> hasStatuses(List<TaskStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) return null;
        return (root, query, cb) -> root.get("status").in(statuses);
    }

    public static Specification<TaskEntity> hasPriorities(List<Integer> priorities) {
        if (priorities == null || priorities.isEmpty()) return null;
        return (root, query, cb) -> root.get("priority").in(priorities);
    }

    public static Specification<TaskEntity> dueFrom(Instant dueFrom) {
        if (dueFrom == null) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("dueAt"), dueFrom);
    }

    public static Specification<TaskEntity> dueTo(Instant dueTo) {
        if (dueTo == null) return null;
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("dueAt"), dueTo);
    }

    public static Specification<TaskEntity> matchesQuery(String q) {
        if (q == null) { return null; }

        String normalized = q.toLowerCase().trim();
        if (normalized.isEmpty()) { return null; }

        String pattern = "%" + normalized + "%";
        return (root, query, criteriaBuilder) -> criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), pattern),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), pattern)
        );
    }
}
