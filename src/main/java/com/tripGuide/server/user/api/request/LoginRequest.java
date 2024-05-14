package com.tripGuide.server.user.api.request;

import javax.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    public String provider;

    @NotBlank
    public String accessToken;
}
