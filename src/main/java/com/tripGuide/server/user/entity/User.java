package com.tripGuide.server.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor @Builder
@NoArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "user_token",
            unique = true,
            nullable = false
    )
    private String userToken;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String provider;

    @Column(
            name = "oauth_id",
            nullable = false
    )
    private String oauthId;

    @Column
    private Integer age;

    @Column
    private String gender;

    public static User of(String userToken, String email, String nickname, String provider, String oauthId) {
        return User.builder()
                .userToken(userToken)
                .email(email)
                .nickname(nickname)
                .provider(provider)
                .oauthId(oauthId)
                .build();
    }
}

