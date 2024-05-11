package com.tripGuide.server.user.api.response;

import com.tripGuide.server.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserProfileResponse {
    private Long id;
    private String userToken;
    private String provider;
    private String nickname;
    private String email;
    private Integer age;
    private String gender;

    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .userToken(user.getUserToken())
                .provider(user.getProvider())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .age(user.getAge())
                .gender(user.getGender())
                .build();
    }
}
