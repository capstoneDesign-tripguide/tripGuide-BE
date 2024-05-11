package com.tripGuide.server.commons.security.token.dto;

import com.tripGuide.server.commons.exception.CustomException;
import com.tripGuide.server.commons.exception.ErrorCode;
import lombok.Getter;

import java.util.Objects;

@Getter
public class JwtAuthentication {

    private Long id;
    private String accessToken;

    public JwtAuthentication(Long id, String accessToken) {
        this.id = validateId(id);
        this.accessToken = validateUserToken(accessToken);
    }

    private Long validateId(Long id) {
        if (Objects.isNull(id) || id <= 0L) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return id;
    }

    private String validateUserToken(String userToken) {
        if (userToken.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        return userToken;
    }

}
