package com.tripGuide.server.commons.security.token.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Tokens {
    private final String accessToken;
    private final String refreshToken;
}
