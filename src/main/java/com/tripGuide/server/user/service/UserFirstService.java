package com.tripGuide.server.user.service;

import com.tripGuide.server.commons.security.oauth.dto.AuthUserInfo;
import com.tripGuide.server.commons.security.oauth.dto.OAuthUserDataResponse;
import com.tripGuide.server.commons.security.oauth.dto.OAuthUserInfo;
import com.tripGuide.server.commons.security.oauth.service.OAuthService;
import com.tripGuide.server.commons.security.token.dto.Tokens;
//import com.tripGuide.server.commons.security.token.service.TokenService;
import com.tripGuide.server.user.api.request.LoginRequest;
import com.tripGuide.server.user.api.response.LoginSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFirstService {
    private final UserService userService;
//    private final TokenService tokenService;
    private final OAuthService oAuthService;

//    public LoginSuccessResponse login(LoginRequest loginRequest) {
//        // login시도가 왔으면 일단 oAuthUserData를 뽑아낸다.
//        OAuthUserDataResponse oAuthUserDataResponse = oAuthService.login(loginRequest);
//
//        OAuthUserInfo oAuthUserInfo = OAuthUserInfo.from(oAuthUserDataResponse);
//
//        // 그러면 이제 이 oAuthUserInfo를 가지고 userService에 등록해야지
//        AuthUserInfo registerResult = userService.getOrRegister(oAuthUserInfo);
//
//        // token을 만들어낸다.
//        Tokens tokens = tokenService.createTokens(registerResult);
//
//        boolean loginSuccess = true;
//
//        return LoginSuccessResponse.of(tokens.getAccessToken(), tokens.getRefreshToken(), loginSuccess);
//    }

    @Transactional
    public void delete(Long userId) {
        userService.delete(userId);
    }
}
