package com.auth_server.service;


import com.auth_server.dto.RegisterRequestDto;
import com.auth_server.dto.UserResponseDto;
import com.auth_server.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    UserResponse getCurrentUser(String name);

    List<UserResponse> getAllUserList();
}
