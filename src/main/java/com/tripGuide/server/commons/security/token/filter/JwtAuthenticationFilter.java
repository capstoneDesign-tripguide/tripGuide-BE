package com.tripGuide.server.commons.security.token.filter;

import com.tripGuide.server.commons.security.token.dto.JwtAuthenticationToken;
import com.tripGuide.server.commons.security.token.service.TokenService;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String BEARER_TYPE = "Bearer";
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = getAccessToken(request);

        if (accessToken != null) {
            JwtAuthenticationToken authentication = tokenService.getAuthenticationByAccessToken(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request,response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().endsWith("/tokens");
    }

    private String getAccessToken(HttpServletRequest request) {
        String authHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeaderValue != null) {
            return extractToken(authHeaderValue);
        }


        return null;
    }

    private String extractToken(String authHeaderValue) {
        if (authHeaderValue.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            return authHeaderValue.substring(BEARER_TYPE.length()).trim();
        }
        return null;
    }
}
