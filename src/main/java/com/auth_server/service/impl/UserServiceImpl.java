package com.auth_server.service.impl;


import com.auth_server.dto.response.UserResponse;
import com.auth_server.entity.User;
import com.auth_server.repository.UserRepository;
import com.auth_server.service.UserService;
import com.auth_server.utils.HelperFn;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final HelperFn helperFn;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserResponse getCurrentUser(String name) {
        User user = userRepository.findByEmail(name).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(auth.getAuthorities());
        return helperFn.buildUserResponse(user);
    }


    @Override
    public List<UserResponse> getAllUserList() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Set<String> currentRole = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());

        List<User> userList = userRepository.findAll();

        if (currentRole.contains("ROLE_ADMIN") && !currentRole.contains("ROLE_SUPERADMIN")) {
            userList = userList.stream()
                    .filter(user -> user.getRole().stream()
                            .noneMatch(role -> role.getRoleName().name().equals("SUPERADMIN"))).toList();
        }

        return userList.stream()
                .map(helperFn::buildUserResponse)
                .toList();
    }


}
