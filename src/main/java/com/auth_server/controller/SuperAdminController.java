package com.auth_server.controller;

import com.auth_server.dto.request.UpdateRoleRequest;
import com.auth_server.dto.response.UserResponse;
import com.auth_server.enums.RoleEnum;
import com.auth_server.service.SuperAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin")
public class SuperAdminController {

    private final SuperAdminService superAdminService;

    @PatchMapping("/update/profile-role/{profileId}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<UserResponse> updateProfileRole(@PathVariable Long profileId, @RequestBody UpdateRoleRequest updateRoleRequest) {
        UserResponse userResponse = superAdminService.updateProfileRole(profileId, updateRoleRequest.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}
