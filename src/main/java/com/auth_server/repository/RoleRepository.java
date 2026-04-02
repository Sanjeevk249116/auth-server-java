package com.auth_server.repository;

import com.auth_server.entity.Role;
import com.auth_server.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(RoleEnum roleName);
}
