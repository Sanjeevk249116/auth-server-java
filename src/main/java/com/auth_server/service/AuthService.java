package com.auth_server.service;

import com.auth_server.dto.request.LoginRequest;
import com.auth_server.dto.request.RefreshTokenRequest;
import com.auth_server.dto.request.RegisterRequest;
import com.auth_server.dto.response.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {


    AuthResponse registerNewUser(@Valid RegisterRequest registerRequest);

    AuthResponse loginUser(@Valid LoginRequest loginRequest);

    AuthResponse generateToken(@Valid RefreshTokenRequest refreshTokenRequest);

    String logoutUser(String name);
}
