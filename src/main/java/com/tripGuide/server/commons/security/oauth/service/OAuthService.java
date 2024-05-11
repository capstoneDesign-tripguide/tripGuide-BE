package com.tripGuide.server.commons.security.oauth.service;

import com.tripGuide.server.user.api.request.LoginRequest;
import com.tripGuide.server.commons.security.oauth.dto.OAuthUserDataRequest;
import com.tripGuide.server.commons.security.oauth.dto.OAuthUserDataResponse;
import com.tripGuide.server.commons.security.oauth.handler.OAuthAuthenticationHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OAuthService {

    private final Map<OAuthProvider, OAuthAuthenticationHandler> oAuthAuthenticationHandlers;

    public OAuthService(List<OAuthAuthenticationHandler> oAuthAuthenticationHandlers) {
        this.oAuthAuthenticationHandlers = oAuthAuthenticationHandlers.stream().collect(
                Collectors.toConcurrentMap(OAuthAuthenticationHandler::getAuthProvider, Function.identity())
        );
    }

    public OAuthUserDataResponse login(LoginRequest loginRequest) {
        OAuthProvider oAuthProvider = OAuthProvider.get(loginRequest.getProvider());

        // 각 oAuth에 맞는 핸들러를 가져온다. (kakao 핸들러를 가져온다.)
        OAuthAuthenticationHandler oAuthHandler = this.oAuthAuthenticationHandlers.get(oAuthProvider);

        // accessToken을 뽑아내서 oAuth핸들러에게 전달한다.
        OAuthUserDataRequest request = new OAuthUserDataRequest(loginRequest.getAccessToken());

        return oAuthHandler.getOAuthUserData(request);
    }
}
