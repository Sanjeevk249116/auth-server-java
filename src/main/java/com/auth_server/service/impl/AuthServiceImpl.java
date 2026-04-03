package com.auth_server.service.impl;

import com.auth_server.dto.request.LoginRequest;
import com.auth_server.dto.request.RefreshTokenRequest;
import com.auth_server.dto.request.RegisterRequest;
import com.auth_server.dto.response.AuthResponse;
import com.auth_server.entity.RefreshToken;
import com.auth_server.entity.Role;
import com.auth_server.entity.User;
import com.auth_server.enums.RoleEnum;
import com.auth_server.exceptionHandling.CustomException;
import com.auth_server.repository.RoleRepository;
import com.auth_server.repository.UserRepository;
import com.auth_server.security.CustomerUserDetail;
import com.auth_server.service.AuthService;
import com.auth_server.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final RefreshTokenImpl refreshTokenImpl;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public AuthResponse registerNewUser(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email already exists with" + registerRequest.getEmail());
        }

        Role role = roleRepository.findByRoleName(RoleEnum.USER).orElseThrow(() -> new IllegalArgumentException("role not found"));
        User newUser = modelMapper.map(registerRequest, User.class);
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(Set.of(role));

        User savedUser = userRepository.save(newUser);

        CustomerUserDetail customerUserDetail = new CustomerUserDetail(savedUser);
        String accessToken = jwtUtils.generateAccessToken(customerUserDetail);

        RefreshToken refreshToken = refreshTokenImpl.createNewRefreshToken(savedUser);
        return buildApiResponse(accessToken, refreshToken.getToken(), customerUserDetail);
    }

    @Override
    @Transactional
    public AuthResponse loginUser(LoginRequest loginRequest) {
        try {

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            CustomerUserDetail customerUserDetail = (CustomerUserDetail) authentication.getPrincipal();

            User user = customerUserDetail.getUser();
            String accessToken = jwtUtils.generateAccessToken(customerUserDetail);
            RefreshToken refreshToken = refreshTokenImpl.createNewRefreshToken(user);

            return buildApiResponse(accessToken, refreshToken.getToken(), customerUserDetail);
        } catch (BadCredentialsException e) {
            throw new CustomException("Invalid email or password", 401);
        } catch (UsernameNotFoundException e) {
            throw new CustomException("User not found", 400);
        }
    }

    @Override
    @Transactional
    public AuthResponse generateToken(RefreshTokenRequest refreshTokenRequest) {
        RefreshToken refreshToken = refreshTokenImpl.verifyRefreshToken(refreshTokenRequest.getRefreshToken());

        User user = refreshToken.getUser();
        CustomerUserDetail customerUserDetail = new CustomerUserDetail(user);
        String accessToken = jwtUtils.generateAccessToken(customerUserDetail);
        RefreshToken refreshToken1 = refreshTokenImpl.createNewRefreshToken(user);
        return buildApiResponse(accessToken, refreshToken1.getToken(), customerUserDetail);

    }

    @Override
    @Transactional
    public String logoutUser(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found with email" + email));
        refreshTokenImpl.deleteByUser(user);
        return "Account has been logged out";
    }

    private AuthResponse buildApiResponse(String accessToken, String token, CustomerUserDetail customerUserDetail) {
        List<String> roles = customerUserDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(token)
                .email(customerUserDetail.getUsername())
                .roles(roles)
                .tokenType("Bearer")
                .expiresIn(jwtUtils.getAccess_token_expiration())
                .build();
    }
}
