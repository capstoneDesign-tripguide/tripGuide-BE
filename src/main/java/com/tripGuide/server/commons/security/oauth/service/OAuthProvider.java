package com.tripGuide.server.commons.security.oauth.service;

import com.tripGuide.server.commons.exception.CustomException;
import com.tripGuide.server.commons.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OAuthProvider {

    KAKAO("KAKAO");

    private final String name;

    public static OAuthProvider get(String name) {
        return Arrays.stream(OAuthProvider.values())
                .filter(provider -> provider.getName().equals(name))
                .findAny()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_PROVIDER));
    }
}
