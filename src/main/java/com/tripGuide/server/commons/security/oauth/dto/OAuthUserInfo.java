package com.tripGuide.server.commons.security.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUserInfo {
    private String provider;
    private String oauthId;
    private String nickname;
    private String email;

    public static OAuthUserInfo from(OAuthUserDataResponse oAuthUserDataResponse) {
        return OAuthUserInfo.builder()
                .provider(oAuthUserDataResponse.getProvider())
                .oauthId(oAuthUserDataResponse.getOauthId())
                .nickname(oAuthUserDataResponse.getNickname())
                .email(oAuthUserDataResponse.getEmail())
                .build();
    }
}
