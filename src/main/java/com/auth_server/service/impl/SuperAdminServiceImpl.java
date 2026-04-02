package com.auth_server.service.impl;

import com.auth_server.dto.response.UserResponse;
import com.auth_server.entity.Role;
import com.auth_server.entity.User;
import com.auth_server.enums.RoleEnum;
import com.auth_server.repository.RoleRepository;
import com.auth_server.repository.UserRepository;
import com.auth_server.service.SuperAdminService;
import com.auth_server.utils.HelperFn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SuperAdminServiceImpl implements SuperAdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HelperFn helperFn;

    @Override
    @Transactional
    public UserResponse updateProfileRole(Long profileId, RoleEnum roleEnum) {

        User user = userRepository.findById(profileId).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        Role role = roleRepository.findByRoleName(roleEnum).orElseThrow(() -> new IllegalArgumentException("Role not found"));

        user.getRole().add(role);
        userRepository.save(user);

        return helperFn.buildUserResponse(user);
    }
}
