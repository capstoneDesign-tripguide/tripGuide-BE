package com.tripGuide.server.user.repository;

import com.tripGuide.server.user.entity.User;

import java.util.Optional;

public interface UserRepsitoryCustom {
    Optional<User> findByUserIdByProviderAndOauthId(String provider, String oauthId);
}
