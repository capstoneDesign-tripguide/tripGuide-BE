package com.tripGuide.server.user.api;

import com.tripGuide.server.commons.security.token.dto.JwtAuthentication;
import com.tripGuide.server.user.api.request.LoginRequest;
import com.tripGuide.server.user.api.response.LoginSuccessResponse;
import com.tripGuide.server.user.api.response.UserProfileResponse;
import com.tripGuide.server.user.service.UserFirstService;
import com.tripGuide.server.user.service.UserService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserFirstService userFirstService;

    @PostMapping
    public LoginSuccessResponse login(@RequestBody @Valid LoginRequest loginRequest) {
        return userFirstService.login(loginRequest);
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(@AuthenticationPrincipal JwtAuthentication user) {
        return userService.getUserProfile(user.getId());
    }

    @DeleteMapping("/me")
    public void deleteUser(@AuthenticationPrincipal JwtAuthentication user) {
        userFirstService.delete(user.getId());
    }
}
