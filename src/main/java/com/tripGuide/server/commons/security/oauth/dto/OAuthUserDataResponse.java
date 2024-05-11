package com.tripGuide.server.commons.security.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OAuthUserDataResponse {
    private String provider;
    private String oauthId;
    private String email;
    private String nickname;
}
