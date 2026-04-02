package com.auth_server.controller;


import com.auth_server.dto.response.UserResponse;
import com.auth_server.entity.User;
import com.auth_server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    @GetMapping("/user/me")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        UserResponse userResponse = userService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok().body(userResponse);
    }


    @GetMapping("/admin/user-list")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userList=userService.getAllUserList();
        return ResponseEntity.ok().body(userList);
    }


}
