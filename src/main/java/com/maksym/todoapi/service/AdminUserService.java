package com.maksym.todoapi.service;

import com.maksym.todoapi.entity.UserEntity;
import com.maksym.todoapi.exception.UserNotFoundException;
import com.maksym.todoapi.model.UserRole;
import com.maksym.todoapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AdminUserService {
    private final UserRepository userRepository;

    public AdminUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Page<UserEntity> list(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Transactional
    public UserEntity updateUserRole(UUID id, Boolean admin) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (admin) {
            user.getRoles().add(UserRole.ADMIN);
        } else {
            user.getRoles().remove(UserRole.ADMIN);
        }

        return user;
    }
}
