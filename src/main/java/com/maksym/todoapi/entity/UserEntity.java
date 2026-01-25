package com.maksym.todoapi.entity;

import com.maksym.todoapi.model.UserRole;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.*;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String hashPassword;

    @Column(name = "created_at", nullable = false)
    private Instant createAt;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Set<UserRole> roles = new HashSet<>();

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY
    )
    private List<ProjectEntity> projects = new ArrayList<>();

    protected UserEntity(){
    }

    public UserEntity(UUID id, String email, String hashPassword, Instant createAt, Set<UserRole> roles) {
        this.id = id;
        this.email = email;
        this.hashPassword = hashPassword;
        this.createAt = createAt;
        if(roles != null) {
            this.roles = roles;
        }
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getHashPassword() { return hashPassword; }
    public Instant getCreateAt() { return createAt; }
    public Set<UserRole> getRoles() { return roles; }
    public List<ProjectEntity> getProjects() { return projects; }
}
