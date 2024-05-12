//package com.tripGuide.server.commons.security.token.service;
//
//import com.amazonaws.services.ec2.model.transform.LaunchTemplateElasticInferenceAcceleratorStaxUnmarshaller;
//import com.tripGuide.server.commons.exception.CustomException;
//import com.tripGuide.server.commons.exception.ErrorCode;
//import com.tripGuide.server.commons.security.oauth.dto.AuthUserInfo;
//import com.tripGuide.server.commons.security.token.api.response.TokenResponse;
//import com.tripGuide.server.commons.security.token.dto.JwtAuthentication;
//import com.tripGuide.server.commons.security.token.dto.JwtAuthenticationToken;
//import com.tripGuide.server.commons.security.token.entity.RefreshToken;
//import com.tripGuide.server.commons.security.token.dto.Tokens;
//import com.tripGuide.server.commons.security.token.repository.RefreshTokenRepository;
//import io.jsonwebtoken.Claims;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class TokenService {
//
//    private static final long ONE_WEEK_IN_MILLI_SECONDS = 604800000L;
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final RefreshTokenRepository refreshTokenRepository;
//
//    @Value("${jwt.expiry-seconds.refresh-token}")
//    private long refreshTokenExpireSeconds;
//
//    public Tokens createTokens(AuthUserInfo authUserInfo) {
//        Long userId = authUserInfo.getUserId();
//        String role = authUserInfo.getRole();
//
//        String accessToken = createAccessToken(userId, role);
//        String refreshToken = createRefreshToken(userId, role);
//
//        return new Tokens(accessToken, refreshToken);
//    }
//
//    private String createAccessToken(Long userId, String role) {
//        return jwtTokenProvider.getAccessToken(userId, role);
//    }
//
//    private String createRefreshToken(Long userId, String role) {
//        long expiredDate = System.currentTimeMillis() + refreshTokenExpireSeconds * 1000;
//
//        RefreshToken refreshToken = RefreshToken.builder()
//                .token(UUID.randomUUID().toString())
//                .userId(userId)
//                .role(role)
//                .expiredAt(expiredDate)
//                .build();
//
//        return refreshTokenRepository.save(refreshToken).getToken();
//    }
//
//    public TokenResponse getAccessTokenByRefreshToken(String refreshToken) {
//        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
//                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
//
//        validateExpiration(token); // 위에는 존재 여부 여기는 만료 시간 체킹
//
//        if (token.getExpiredAt() < System.currentTimeMillis() + ONE_WEEK_IN_MILLI_SECONDS) {
//            String renewRefreshToken = UUID.randomUUID().toString();
//            token.renew(renewRefreshToken, refreshTokenExpireSeconds);
//
//            refreshTokenRepository.save(token);
//        }
//
//        String accessToken = createAccessToken(token.getUserId(), token.getRole());
//        return new TokenResponse(accessToken, token.getToken());
//    }
//
//    public void validateExpiration(RefreshToken token) {
//        long expiredAt = token.getExpiredAt();
//        long currentTime = System.currentTimeMillis();
//
//        if (expiredAt < currentTime) {
//            throw new CustomException(ErrorCode.REFRESH_TOKEN_EXPIRED);
//        }
//    }
//
//    public JwtAuthenticationToken getAuthenticationByAccessToken(String accessToken) {
//        jwtTokenProvider.validationToken(accessToken);
//
//        Claims claims = jwtTokenProvider.getClaims(accessToken);
//
//        Long id = claims.get("userId", Long.class);
//        String role = claims.get("role", String.class);
//
//        JwtAuthentication principal = new JwtAuthentication(id, accessToken);
//        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));
//
//        return new JwtAuthenticationToken(principal, null, authorities);
//    }
//
//    public void deleteRefreshToken(String refreshToken) {
//        refreshTokenRepository.findByToken(refreshToken)
//                .ifPresent(refreshTokenRepository::delete);
//    }
//}
