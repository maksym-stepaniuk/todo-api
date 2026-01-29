package com.maksym.todoapi.controller;

import com.maksym.todoapi.dto.AdminUserResponse;
import com.maksym.todoapi.dto.PageResponse;
import com.maksym.todoapi.dto.RoleUpdateRequest;
import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.service.AdminUserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminUserService adminUserService;

    public AdminController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<AdminUserResponse> listUsers(Pageable pageable) {
        Page<UserEntity> page = adminUserService.list(pageable);

        return new PageResponse<>(
                page.getContent().stream()
                        .map(u -> new AdminUserResponse(u.getId(), u.getEmail(), u.getRoles(), u.getCreateAt()))
                        .toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast()
        );
    }

    @PatchMapping("/users/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminUserResponse> updateRole(@PathVariable UUID id, @Valid @RequestBody RoleUpdateRequest request) {
        UserEntity user = adminUserService.updateUserRole(id, request.getAdmin());

        return ResponseEntity.ok()
                .body(new AdminUserResponse(user.getId(), user.getEmail(), user.getRoles(), user.getCreateAt()));
    }
}
