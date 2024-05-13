package com.tripGuide.server.commons.security.token.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import java.io.Serializable;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken implements Serializable {

    @Id
    private Long userId;

    private String token;

    private String role;

    private long expiredAt;

    @Builder
    private RefreshToken(String token, Long userId, String role, long expiredAt) {
        this.token = token;
        this.userId = userId;
        this.role = role;
        this.expiredAt = expiredAt;
    }

    public void renew(String token, long expiredAt) {
        this.token = token;
        this.expiredAt = expiredAt;
    }
}
