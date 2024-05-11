package com.tripGuide.server.commons.security.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuthUserDataRequest {
    private String accessToken;
}
