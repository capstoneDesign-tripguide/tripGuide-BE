package com.tripGuide.server.commons.security.token.repository;

import com.tripGuide.server.commons.security.token.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(Long userId);
}
