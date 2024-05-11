package com.tripGuide.server.commons.security.oauth.handler;


import com.tripGuide.server.commons.exception.CustomException;
import com.tripGuide.server.commons.exception.ErrorCode;
import com.tripGuide.server.commons.security.oauth.dto.KakaoUserData;
import com.tripGuide.server.commons.security.oauth.dto.OAuthUserDataRequest;
import com.tripGuide.server.commons.security.oauth.dto.OAuthUserDataResponse;
import com.tripGuide.server.commons.security.oauth.service.OAuthProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class KakaoService implements OAuthAuthenticationHandler {

    private final RestTemplate restTemplate;

    @Value("${spring.security.oauth2.kakao.host}")
    private String host;

    @Override
    public OAuthProvider getAuthProvider() {
        return OAuthProvider.KAKAO;
    }

    @Override
    public OAuthUserDataResponse getOAuthUserData(OAuthUserDataRequest request) {
        String url = host + "/v2/user/me";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", "Bearer " + request.getAccessToken());

        HttpEntity<?> httpRequest = new HttpEntity<>(null, httpHeaders);

        try {
            ResponseEntity<KakaoUserData> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpRequest,
                    KakaoUserData.class
            );

            KakaoUserData userData = response.getBody();
            return OAuthUserDataResponse.builder()
                    .provider(getAuthProvider().toString())
                    .oauthId(userData.getId().toString())
                    .email(userData.getEmail())
                    .nickname(userData.getNickname())
                    .build();

        } catch (RestClientException e) {
            log.warn("[KakaoService] failed to get OAuth User Data = {}", request.getAccessToken());
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }
}
