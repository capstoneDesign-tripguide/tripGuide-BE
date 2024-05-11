package com.tripGuide.server.commons.security.oauth.handler;

import com.tripGuide.server.commons.security.oauth.dto.OAuthUserDataRequest;
import com.tripGuide.server.commons.security.oauth.dto.OAuthUserDataResponse;
import com.tripGuide.server.commons.security.oauth.service.OAuthProvider;

public interface OAuthAuthenticationHandler {
    OAuthProvider getAuthProvider();

    OAuthUserDataResponse getOAuthUserData(OAuthUserDataRequest request);
}
