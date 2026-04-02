package com.auth_server.service;

import com.auth_server.dto.response.UserResponse;
import com.auth_server.enums.RoleEnum;
import org.springframework.stereotype.Service;

@Service
public interface SuperAdminService {
    UserResponse updateProfileRole(Long profileId, RoleEnum role);
}
