package com.tripGuide.server.commons.security.token.entity;

import com.tripGuide.server.commons.domain.TimeBaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends TimeBaseEntity {

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
