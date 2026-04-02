package com.auth_server.utils;

import com.auth_server.dto.response.UserResponse;
import com.auth_server.entity.User;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class HelperFn {
    public UserResponse buildUserResponse(User user) {
        Set<String> role = user.getRole().stream().map(item -> item.getRoleName().name()).collect(Collectors.toSet());
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .roles(role)
                .build();
    }
}
