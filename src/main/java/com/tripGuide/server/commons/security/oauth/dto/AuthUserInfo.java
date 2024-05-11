package com.tripGuide.server.commons.security.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AuthUserInfo {
    private final Long userId;
    private final String email;
    private final String nickname;
    private final String role;

    public static AuthUserInfo from(Long userId, String email, String nickname, String role) {
        return AuthUserInfo.builder()
                .userId(userId)
                .email(email)
                .nickname(nickname)
                .role(role)
                .build();
    }
}
