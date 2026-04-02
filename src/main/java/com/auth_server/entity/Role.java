package com.auth_server.entity;

import com.auth_server.enums.RoleEnum;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true,name = "role_name")
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

    @CreationTimestamp
    private LocalDateTime createdAt;

}
