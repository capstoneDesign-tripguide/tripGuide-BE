package com.tripGuide.server.commons.security.token.service;

import com.tripGuide.server.commons.exception.CustomException;
import com.tripGuide.server.commons.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {

    private static final long MILLI_SECOND = 1000L;

    private final String issuer;
    private final String secretKey;
    private final long accessTokenExpirySeconds;

    public JwtTokenProvider(
            @Value("${jwt.issuer}") String issuer,
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.expiry-seconds.access-token}") long accessTokenExpirySeconds
    ) {
        this.issuer = issuer;
        this.secretKey = secretKey;
        this.accessTokenExpirySeconds = accessTokenExpirySeconds;
    }

    public String getAccessToken(Long userId, String role) {
        Map<String, Object> claims = Map.of("userId", userId, "role", role);
        return this.createAccessToken(claims);
    }

    public String createAccessToken(Map<String, Object> claims) {
        Date now = new Date();
        Date expiredDate = new Date(now.getTime() + accessTokenExpirySeconds * MILLI_SECOND);

        return Jwts.builder()
                .setIssuer(issuer)
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public void validationToken(String token) {
        try {
                Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        } catch (JwtException | IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
    }

}
