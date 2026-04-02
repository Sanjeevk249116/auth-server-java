package com.auth_server.dto.request;

import com.auth_server.enums.RoleEnum;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    private RoleEnum role;
}

