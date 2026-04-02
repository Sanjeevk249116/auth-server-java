package com.auth_server.repository;

import com.auth_server.entity.RefreshToken;
import com.auth_server.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    void deleteByUser(User user);
    Optional<RefreshToken> findByToken(String token);
}
